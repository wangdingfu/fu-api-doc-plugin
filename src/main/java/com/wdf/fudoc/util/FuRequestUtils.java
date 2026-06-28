package com.wdf.fudoc.util;

import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.common.constant.FuDocConstants;

import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-21 23:37:03
 */
public class FuRequestUtils {

    /**
     * 判断Controller中的方法是否为http接口
     *
     * @param psiMethod 方法体
     * @return true：是http接口
     */
    public static boolean isHttpMethod(PsiMethod psiMethod) {
        return Objects.nonNull(getMapping(psiMethod));
    }

    public static String getMapping(PsiMethod psiMethod) {
        for (String mapping : AnnotationConstants.MAPPING) {
            if (psiMethod.hasAnnotation(mapping)) {
                return mapping;
            }
        }
        return null;
    }



    /**
     * 获取http请求持久化路径
     *
     * @param project  当前项目
     * @param psiClass controller
     * @return 持久化目录
     */
    public static String getRequestPath(Project project, PsiClass psiClass) {
        String basePath = project.getBasePath();
        if (FuStringUtils.isBlank(basePath)) {
            basePath = FileUtil.getTmpDir().getPath();
        }
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleName = Objects.isNull(module) ? FuStringUtils.EMPTY : module.getName();
        String controllerName = psiClass.getName();
        return Paths.get(basePath, FuDocConstants.IDEA_DIR, FuDocConstants.FU_DOC, FuDocConstants.API_DIR, moduleName, controllerName).toString();
    }

}
