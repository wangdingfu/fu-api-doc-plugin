package com.wdf.fudoc.spring;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.request.pojo.SpringBootEnvConfigInfo;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.MavenUtils;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringBoot环境信息加载
 *
 * @author wangdingfu
 * @date 2023-07-22 17:30:13
 */
@Slf4j
public class SpringBootEnvLoader {

    private static final Map<Project, SpringBootEnvModuleInfo> SPRING_BOOT_MODULE = new ConcurrentHashMap<>();


    /**
     * 加载springboot相关配置信息
     *
     * @param project 当前项目
     */
    public static void doLoad(Project project, boolean isForceLoad, boolean isReload) {
        if (DumbService.isDumb(project)) {
            log.info("当前正在加载索引....");
            return;
        }
        if (isForceLoad) {
            initSpringBoot(project, true, isReload);
        } else {
            if (Objects.isNull(SPRING_BOOT_MODULE.get(project))) {
                initSpringBoot(project, false, isReload);
            }
        }
    }


    /**
     * 获取当前项目启动类
     *
     * @param project 当前项目
     * @return 启动类集合
     */
    public static Set<String> getApplication(Project project) {
        if (Objects.isNull(project)) {
            return Sets.newHashSet();
        }
        SpringBootEnvModuleInfo springBootEnvModuleInfo = getEnvInfo(project);
        if (Objects.isNull(springBootEnvModuleInfo)) {
            return Sets.newHashSet();
        }
        return springBootEnvModuleInfo.getApplication();
    }


    public static String getApplication(Module module) {
        SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = getEnvInfo(module);
        if (Objects.isNull(envInfo)) {
            Set<String> application = getApplication(module.getProject());
            if (application.size() == 1) {
                return application.iterator().next();
            }
            return SpringConfigFileConstants.APPLICATION;
        }
        return envInfo.getApplicationName();
    }


    public static Integer getServerPort(Module module) {
        SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = getEnvInfo(module);
        if (Objects.isNull(envInfo)) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }
        String defaultEnv = envInfo.getDefaultEnv();
        Map<String, SpringBootEnvConfigInfo> envConfigInfoMap = envInfo.getEnvConfigInfoMap();
        if (StringUtils.isBlank(defaultEnv) || MapUtils.isEmpty(envConfigInfoMap)) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }
        SpringBootEnvConfigInfo springBootEnvConfigInfo = envConfigInfoMap.get(defaultEnv);
        return Objects.isNull(springBootEnvConfigInfo) ? SpringConfigFileConstants.DEFAULT_SERVER_PORT : springBootEnvConfigInfo.getServerPort();
    }


    public static SpringBootEnvModuleInfo.SpringBootEnvInfo getEnvInfo(Module module) {
        if (Objects.isNull(module)) {
            return null;
        }
        SpringBootEnvModuleInfo springBootEnvModuleInfo = getEnvInfo(module.getProject());
        if (Objects.isNull(springBootEnvModuleInfo)) {
            return null;
        }
        return springBootEnvModuleInfo.getEnvInfo(module);
    }


    public static SpringBootEnvModuleInfo getEnvInfo(Project project) {
        SpringBootEnvModuleInfo springBootEnvModuleInfo = SPRING_BOOT_MODULE.get(project);
        if (Objects.isNull(springBootEnvModuleInfo)) {
            //初始化该module信息
            return initSpringBoot(project, false, false);
        }
        return springBootEnvModuleInfo;
    }


    public static SpringBootEnvModuleInfo initSpringBoot(Project project, boolean isForceLoad, boolean isReload) {
        SpringBootEnvModuleInfo springBootEnvModuleInfo = ApplicationManager.getApplication().runReadAction((Computable<SpringBootEnvModuleInfo>) () -> doInitSpringBoot(project));
        if (Objects.nonNull(springBootEnvModuleInfo)) {
            SPRING_BOOT_MODULE.put(project, springBootEnvModuleInfo);
            loadSpringBootConfig(project, isForceLoad, isReload);
        }
        return springBootEnvModuleInfo;
    }


    /**
     * 初始化指定项目的springboot项目信息
     *
     * @param project 指定项目
     */
    private static SpringBootEnvModuleInfo doInitSpringBoot(Project project) {
        Collection<PsiAnnotation> psiAnnotations;
        try {
            psiAnnotations = JavaAnnotationIndex.getInstance().get(AnnotationConstants.SPRING_BOOT_APPLICATION, project, GlobalSearchScope.projectScope(project));
        } catch (IndexNotReadyException e) {
            //缓存还未加载完毕 后续在加载
            log.info("缓存还未加载完成......");
            return null;
        } catch (Exception e) {
            log.info("加载SpringBoot配置异常", e);
            return null;
        }
        if (CollectionUtils.isEmpty(psiAnnotations)) {
            return null;
        }
        //初始化module之间的关系
        Collection<Module> modules = ModuleUtil.getModulesOfType(project, JavaModuleType.getModuleType());
        SpringBootEnvModuleInfo springBootEnvModuleInfo = new SpringBootEnvModuleInfo();
        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            if (!AnnotationConstants.SPRING_BOOT_APPLICATION_ANNOTATION.equals(psiAnnotation.getQualifiedName())) {
                continue;
            }
            PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
            PsiElement psiElement = psiModifierList.getParent();
            if (Objects.isNull(psiElement) || !(psiElement instanceof PsiClass psiClass)) {
                continue;
            }
            String applicationName = psiClass.getName();
            Module module = ModuleUtil.findModuleForPsiElement(psiElement);
            SpringConfigFile springConfigFile = SpringConfigManager.doLoadSpringConfig(module);
            Set<String> envs = springConfigFile.getEnvs();
            if (CollectionUtils.isEmpty(envs)) {
                envs = Sets.newHashSet(SpringConfigFileConstants.DEFAULT_ENV);
            }
            if (envs.size() > 1) {
                envs.remove(SpringConfigFileConstants.DEFAULT_ENV);
            }
            String activeEnv = springConfigFile.getActiveEnv();
            if (!envs.contains(activeEnv)) {
                activeEnv = envs.iterator().next();
            }
            Map<String, SpringBootEnvConfigInfo> envConfigInfoMap = new HashMap<>();
            envs.forEach(f -> envConfigInfoMap.put(f, new SpringBootEnvConfigInfo(springConfigFile.getServerPort(f), springConfigFile.getConfig(SpringConfigFileConstants.CONTEXT_PATH_KEY))));
            springBootEnvModuleInfo.addEnvInfo(module, applicationName, activeEnv, envConfigInfoMap, MavenUtils.getChildModule(module, modules));
        }
        return springBootEnvModuleInfo;
    }


    /**
     * 加载Spring环境信息
     *
     * @param project     当前项目
     * @param isForceLoad 是否强制加载（不看配置 如果为true则必须加载）
     */
    public static void loadSpringBootConfig(Project project, boolean isForceLoad, boolean isReLoad) {
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        if (!isForceLoad && !fuRequestConfigPO.isAutoPort()) {
            return;
        }
        //自动读取springboot配置并使用
        SpringBootEnvModuleInfo springBootEnvModuleInfo = SPRING_BOOT_MODULE.get(project);
        if (Objects.isNull(springBootEnvModuleInfo)) {
            //读取配置异常
            log.info("读取SpringBoot配置文件失败");
            return;
        }
        if (!isForceLoad && springBootEnvModuleInfo.isLoad()) {
            //已加载 无需重复加载
            return;
        }
        List<ConfigEnvTableBO> envConfigList = fuRequestConfigPO.getEnvConfigList();
        Map<String, ConfigEnvTableBO> configEnvMap = ObjectUtils.listToMap(envConfigList, envTableBO -> envTableBO.getApplication() + ":" + envTableBO.getEnvName());
        springBootEnvModuleInfo.getEnvMap().forEach((key, value) -> {
            String applicationName = value.getApplicationName();
            Map<String, SpringBootEnvConfigInfo> envConfigInfoMap = value.getEnvConfigInfoMap();
            if (MapUtils.isEmpty(envConfigInfoMap)) {
                return;
            }
            String moduleName = key.getName();
            String defaultEnv = fuRequestConfigPO.getEnv(moduleName);
            if (StringUtils.isBlank(defaultEnv) || !envConfigInfoMap.containsKey(defaultEnv)) {
                defaultEnv = value.getDefaultEnv();
                if (!envConfigInfoMap.containsKey(defaultEnv)) {
                    defaultEnv = envConfigInfoMap.keySet().iterator().next();
                }
                value.setDefaultEnv(defaultEnv);
            }

            fuRequestConfigPO.addDefaultEnv(key.getName(), defaultEnv);
            envConfigInfoMap.forEach((envName, envConfigInfo) -> {
                String configEnvKey = applicationName + ":" + envName;
                ConfigEnvTableBO configEnvTableBO = configEnvMap.get(configEnvKey);
                String contextPath = envConfigInfo.getContextPath();
                String contextPathUrl = StringUtils.isBlank(contextPath) ? StringUtils.EMPTY : "/" + contextPath;
                String domain = "http://localhost:" + envConfigInfo.getServerPort() + contextPathUrl;
                if (Objects.isNull(configEnvTableBO)) {
                    configEnvTableBO = new ConfigEnvTableBO();
                    configEnvTableBO.setSelect(true);
                    configEnvTableBO.setEnvName(envName);
                    configEnvTableBO.setApplication(applicationName);
                    configEnvTableBO.setDomain(domain);
                    envConfigList.add(configEnvTableBO);
                } else {
                    if (isReLoad) {
                        configEnvTableBO.setDomain(domain);
                    }
                }
            });
        });
        springBootEnvModuleInfo.setLoad(true);
    }


}
