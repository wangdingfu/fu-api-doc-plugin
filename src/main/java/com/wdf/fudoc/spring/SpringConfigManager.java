package com.wdf.fudoc.spring;

import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring配置文件管理
 *
 * @author wangdingfu
 * @date 2022-11-19 13:48:16
 */
public class SpringConfigManager {

    private static final Map<Module, SpringConfigFile> MODULE_SPRING_CONFIG_MAP = new ConcurrentHashMap<>();

    private static final Map<Project, Map<Module, String>> SPRING_APPLICATION_MAP = new ConcurrentHashMap<>();

    public static Set<String> getApplicationList(Project project) {
        Map<Module, String> moduleStringMap = SPRING_APPLICATION_MAP.get(project);
        if (MapUtils.isEmpty(moduleStringMap)) {
            return Sets.newHashSet();
        }
        return Sets.newHashSet(moduleStringMap.values());
    }


    public static String getApplication(Module module) {
        Map<Module, String> moduleStringMap = SPRING_APPLICATION_MAP.get(module.getProject());
        if (MapUtils.isEmpty(moduleStringMap)) {
            return StringUtils.EMPTY;
        }
        String applicationName = moduleStringMap.get(module);
        return Objects.isNull(applicationName) ? StringUtils.EMPTY : applicationName;
    }

    /**
     * 初始化执行项目下所有的spring配置文件到内存中
     *
     * @param project 当前项目
     */
    public static void initProjectSpringConfig(Project project) {
        ApplicationManager.getApplication().runReadAction(() -> {
            //初始化Springboot应用
            initSpringBoot(project);
        });

    }


    private static void initSpringBoot(Project project) {
        Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(AnnotationConstants.SPRING_BOOT_APPLICATION, project, GlobalSearchScope.projectScope(project));
        if (CollectionUtils.isNotEmpty(psiAnnotations)) {
            Map<Module, PsiClass> springBootApplicationMap = new HashMap<>();
            Map<Module, String> moduleApplicationMap = new HashMap<>();
            SPRING_APPLICATION_MAP.put(project, moduleApplicationMap);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                if (!AnnotationConstants.SPRING_BOOT_APPLICATION_ANNOTATION.equals(psiAnnotation.getQualifiedName())) {
                    continue;
                }
                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                PsiElement psiElement = psiModifierList.getParent();
                if (Objects.isNull(psiElement) || !(psiElement instanceof PsiClass psiClass)) {
                    continue;
                }
                Module module = ModuleUtil.findModuleForPsiElement(psiElement);
                springBootApplicationMap.put(module, psiClass);
                moduleApplicationMap.put(module, psiClass.getName());
            }

            if (MapUtils.isEmpty(springBootApplicationMap)) {
                return;
            }
            FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorageFactory.get(project).readData();
            if (!fuRequestConfigPO.isAutoPort()) {
                //不自动读取端口信息
                return;
            }

            List<ConfigEnvTableBO> envConfigList = fuRequestConfigPO.getEnvConfigList();
            springBootApplicationMap.forEach((module, springFile) -> {
                //Spring启动类名称
                String springBootName = springFile.getName();
                SpringConfigFile springConfigFile = initSpringConfig(module);
                Set<String> envs = springConfigFile.getEnvs();
                if (CollectionUtils.isEmpty(envs)) {
                    envs = Sets.newHashSet(SpringConfigFileConstants.DEFAULT_ENV);
                }
                if (envs.size() > 1) {
                    envs.remove(SpringConfigFileConstants.DEFAULT_ENV);
                }
                for (String env : envs) {
                    if (envConfigList.stream().anyMatch(a -> a.getEnvName().equals(env) && a.getApplication().equals(springBootName))) {
                        continue;
                    }
                    ConfigEnvTableBO envTableBO = new ConfigEnvTableBO();
                    envTableBO.setApplication(springBootName);
                    envTableBO.setEnvName(env);
                    envTableBO.setSelect(true);
                    envTableBO.setDomain("http://localhost:" + springConfigFile.getConfig(SpringConfigFileConstants.SERVER_PORT_KEY));
                    envConfigList.add(envTableBO);
                }
            });
        }
    }


    /**
     * 从spring配置文件中读取当前模块的端口
     *
     * @param module 所属的java模块
     * @return 当前项目的端口
     */
    public static Integer getServerPort(Module module) {
        String configValue = getConfigValue(module, SpringConfigFileConstants.SERVER_PORT_KEY);
        return StringUtils.isBlank(configValue) ? SpringConfigFileConstants.DEFAULT_SERVER_PORT : Integer.parseInt(configValue);
    }


    public static String getConfigValue(Module module, String configKey) {
        SpringConfigFile springConfigFile = MODULE_SPRING_CONFIG_MAP.get(module);
        if (Objects.isNull(springConfigFile)) {
            //初始化当前模块的配置文件到内存中
            springConfigFile = initSpringConfig(module);
            MODULE_SPRING_CONFIG_MAP.put(module, springConfigFile);
        }
        return springConfigFile.getConfig(configKey);
    }


    /**
     * 初始化指定模块下的配置文件
     *
     * @param module 指定java模块
     * @return 初始化后的配置文件
     */
    public static SpringConfigFile initSpringConfig(Module module) {
        SpringConfigFile springConfigFile = new SpringConfigFile();
        //获取当前模块下的resource目录
        VirtualFile resourceDir = getResourceDir(module);
        if (Objects.nonNull(resourceDir)) {
            //获取resource目录下所有的文件
            for (VirtualFile child : resourceDir.getChildren()) {
                if (!child.isDirectory() && SpringConfigFileConstants.EXTENSIONS.contains(child.getExtension())) {
                    String configFileName = StringUtils.substringBeforeLast(child.getName(), ".");
                    if (configFileName.contains(SpringConfigFileConstants.SPLIT)) {
                        configFileName = StringUtils.substringBeforeLast(configFileName, SpringConfigFileConstants.SPLIT);
                    }
                    if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(configFileName)) {
                        //配置文件
                        springConfigFile.addConfigFile(child);
                    }
                }
            }
        }
        springConfigFile.setModule(module);
        return springConfigFile;
    }


    /**
     * 获取指定java module下resource的目录
     *
     * @param module java module
     * @return resource目录
     */
    private static VirtualFile getResourceDir(Module module) {
        VirtualFile[] rootList = ModuleRootManager.getInstance(module).getSourceRoots(false);
        for (VirtualFile virtualFile : rootList) {
            if (SpringConfigFileConstants.RESOURCE.equals(virtualFile.getName())) {
                return virtualFile;
            }
        }
        return null;
    }


    public static VirtualFile getFile(Module module, String fileName) {
        VirtualFile[] rootList = ModuleRootManager.getInstance(module).getSourceRoots(false);
        for (VirtualFile virtualFile : rootList) {
            if (SpringConfigFileConstants.RESOURCE.equals(virtualFile.getName())) {
                return virtualFile;
            }
        }
        return null;
    }


}
