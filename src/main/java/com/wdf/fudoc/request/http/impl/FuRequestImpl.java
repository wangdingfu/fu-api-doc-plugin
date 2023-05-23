package com.wdf.fudoc.request.http.impl;

import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.FuRequest;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-21 22:59:06
 */
public class FuRequestImpl implements FuRequest {

    private final PsiClass psiClass;

    private final PsiMethod psiMethod;

    private final FuHttpClient fuHttpClient;

    private final Project project;

    public FuRequestImpl(FuHttpClient fuHttpClient) {
        this(fuHttpClient.getPsiClass(), fuHttpClient.getPsiMethod(), fuHttpClient, fuHttpClient.getProject());
    }

    public FuRequestImpl(PsiClass psiClass, PsiMethod psiMethod, FuHttpClient fuHttpClient, Project project) {
        this.psiClass = psiClass;
        this.psiMethod = psiMethod;
        this.fuHttpClient = fuHttpClient;
        this.project = project;
    }

    /**
     * 获取当前请求记录持久化路径
     *
     * @return http文件路径（${projectPath}/.idea/fudoc/api/${moduleName}/${controllerName}）
     */
    @Override
    public String getPath() {
        String basePath = project.getBasePath();
        if (StringUtils.isBlank(basePath)) {
            basePath = FileUtil.getTmpDir().getPath();
        }
        Module module = ModuleUtil.findModuleForPsiElement(psiMethod);
        String moduleName = Objects.isNull(module) ? StringUtils.EMPTY : module.getName();
        String controllerName = psiClass.getName();
        return Paths.get(basePath, FuDocConstants.IDEA_DIR, FuDocConstants.FU_DOC, FuDocConstants.API_DIR, moduleName, controllerName).toString();
    }

    @Override
    public String getHttpFileName() {
        return psiMethod.getName();
    }



    @Override
    public String httpContent() {


        return null;
    }
}
