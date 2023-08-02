package com.wdf.fudoc.util;

import cn.hutool.core.io.FileUtil;
import com.intellij.ws.http.request.HttpRequestPsiFile;
import com.intellij.ws.http.request.HttpRequestPsiUtils;
import com.intellij.ws.http.request.psi.HttpRequest;
import com.intellij.ws.http.request.psi.HttpRequestBlock;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.common.constant.FuDocConstants;
import org.apache.commons.lang3.StringUtils;

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
     * 根据光标位置从.http或.rest文件中读取指定的接口
     *
     * @param httpRequestPsiFile httpClient文件
     * @return 光标所在的接口对象
     */
    public static HttpRequest getHttpRequest(HttpRequestPsiFile httpRequestPsiFile, Editor editor) {
        HttpRequestBlock[] requestBlocks = HttpRequestPsiUtils.getRequestBlocks(httpRequestPsiFile);
        if (requestBlocks.length == 0) {
            return null;
        }
        if (requestBlocks.length == 1) {
            return requestBlocks[0].getRequest();
        }
        int offset = editor.getCaretModel().getOffset();
        for (int i = 1; i < requestBlocks.length; i++) {
            int textOffset = requestBlocks[i].getTextOffset();
            if (offset < textOffset) {
                return requestBlocks[i - 1].getRequest();
            }
        }
        return requestBlocks[requestBlocks.length - 1].getRequest();
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
        if (StringUtils.isBlank(basePath)) {
            basePath = FileUtil.getTmpDir().getPath();
        }
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleName = Objects.isNull(module) ? StringUtils.EMPTY : module.getName();
        String controllerName = psiClass.getName();
        return Paths.get(basePath, FuDocConstants.IDEA_DIR, FuDocConstants.FU_DOC, FuDocConstants.API_DIR, moduleName, controllerName).toString();
    }



    /**
     * 根据url路径从.http或.rest文件中读取指定的接口
     *
     * @param httpRequestPsiFile httpClient文件
     * @param url                接口url
     * @return 接口url对应的接口对象
     */
    public static HttpRequest getHttpRequest(HttpRequestPsiFile httpRequestPsiFile, String url) {
        return null;
    }
}
