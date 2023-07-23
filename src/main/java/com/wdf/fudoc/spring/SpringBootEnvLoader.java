package com.wdf.fudoc.spring;

import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
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
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.MavenUtils;
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
    public static void doLoad(Project project) {
        if (Objects.isNull(SPRING_BOOT_MODULE.get(project))) {
            initSpringBoot(project);
        }
        ApplicationManager.getApplication().invokeLater(() -> loadSpringBootConfig(project));
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
        Map<String, Integer> serverPortMap = envInfo.getServerPortMap();
        if (StringUtils.isBlank(defaultEnv) || MapUtils.isEmpty(serverPortMap)) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }
        Integer serverPort = serverPortMap.get(defaultEnv);
        return Objects.isNull(serverPort) ? SpringConfigFileConstants.DEFAULT_SERVER_PORT : serverPort;
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
            return initSpringBoot(project);
        }
        return springBootEnvModuleInfo;
    }


    public static SpringBootEnvModuleInfo initSpringBoot(Project project) {
        SpringBootEnvModuleInfo springBootEnvModuleInfo = ApplicationManager.getApplication().runReadAction((Computable<SpringBootEnvModuleInfo>) () -> doInitSpringBoot(project));
        if (Objects.nonNull(springBootEnvModuleInfo)) {
            ApplicationManager.getApplication().invokeLater(() -> loadSpringBootConfig(project));
            SPRING_BOOT_MODULE.put(project, springBootEnvModuleInfo);
        }
        return springBootEnvModuleInfo;
    }


    /**
     * 初始化指定项目的springboot项目信息
     *
     * @param project 指定项目
     */
    private static SpringBootEnvModuleInfo doInitSpringBoot(Project project) {
        Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(AnnotationConstants.SPRING_BOOT_APPLICATION, project, GlobalSearchScope.projectScope(project));
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
            SpringConfigFile springConfigFile = SpringConfigManager.initSpringConfig(module);
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
            Map<String, Integer> serverPortMap = new HashMap<>();
            envs.forEach(f -> serverPortMap.put(f, springConfigFile.getServerPort(f)));
            springBootEnvModuleInfo.addEnvInfo(module, applicationName, activeEnv, serverPortMap, MavenUtils.getChildModule(module, modules));
        }
        return springBootEnvModuleInfo;
    }


    public static void loadSpringBootConfig(Project project) {
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        if (!fuRequestConfigPO.isAutoPort()) {
            return;
        }
        //自动读取springboot配置并使用
        SpringBootEnvModuleInfo springBootEnvModuleInfo = SPRING_BOOT_MODULE.get(project);
        if (Objects.isNull(springBootEnvModuleInfo)) {
            //读取配置异常
            log.error("读取SpringBoot配置文件失败");
            return;
        }
        if (springBootEnvModuleInfo.isLoad()) {
            //已加载 无需重复加载
            return;
        }
        List<ConfigEnvTableBO> envConfigList = fuRequestConfigPO.getEnvConfigList();
        springBootEnvModuleInfo.getEnvMap().forEach((key, value) -> {
            String applicationName = value.getApplicationName();
            //移除当前已存在的配置
            envConfigList.removeIf(f -> applicationName.equals(f.getApplication()));
            Map<String, Integer> serverPortMap = value.getServerPortMap();
            if (MapUtils.isEmpty(serverPortMap)) {
                return;
            }
            String moduleName = key.getName();
            String defaultEnv = fuRequestConfigPO.getEnv(moduleName);
            if (StringUtils.isBlank(defaultEnv) || !serverPortMap.containsKey(defaultEnv)) {
                defaultEnv = value.getDefaultEnv();
                if (!serverPortMap.containsKey(defaultEnv)) {
                    defaultEnv = serverPortMap.keySet().iterator().next();
                }
                value.setDefaultEnv(defaultEnv);
            }

            fuRequestConfigPO.addDefaultEnv(key.getName(), defaultEnv);
            serverPortMap.forEach((envName, serverPort) -> {
                ConfigEnvTableBO configEnvTableBO = new ConfigEnvTableBO();
                configEnvTableBO.setSelect(true);
                configEnvTableBO.setEnvName(envName);
                configEnvTableBO.setApplication(applicationName);
                configEnvTableBO.setDomain("http://localhost:" + serverPort);
                envConfigList.add(configEnvTableBO);
            });
        });
        springBootEnvModuleInfo.setLoad(true);
    }


}
