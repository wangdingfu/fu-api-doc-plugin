package com.wdf.fudoc.util;

import com.intellij.httpClient.http.request.HttpRequestLanguage;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.HttpRequestPsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.wdf.fudoc.request.http.FuRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-23 17:46:07
 */
@Slf4j
public class StorageUtils {


    /**
     * 持久化接口信息到http文件上
     *
     * @param fuRequest 请求记录
     */
    public static void writeHttp(FuRequest fuRequest) {
        try {
            //获取或则创建目录
            VirtualFile controllerFile = VfsUtil.createDirectoryIfMissing(fuRequest.getPath());
            if (Objects.isNull(controllerFile)) {
                log.info("持久化目录不存在");
                return;
            }

            //获取http文件对象(不存在直接创建)
            VirtualFile httpVirtualFile = controllerFile.findOrCreateChildData(null, fuRequest.getHttpFileName());

            //将接口信息写入到http文件中
            VfsUtil.saveText(httpVirtualFile, fuRequest.httpContent());
        } catch (IOException e) {
            log.info("持久化http文件异常", e);
        }
    }


    /**
     * 读取http文件
     *
     * @param project   项目
     * @param psiClass  controller
     * @param psiMethod 接口方法体
     * @return http文件内容
     */
    public static HttpRequestPsiFile readHttp(Project project, PsiClass psiClass, PsiMethod psiMethod) {
        final FileViewProviderFactory factory = LanguageFileViewProviders.INSTANCE.forLanguage(HttpRequestLanguage.INSTANCE);
        String requestPath = FuRequestUtils.getRequestPath(project, psiClass);
        VirtualFile file = VfsUtil.findFile(Paths.get(requestPath, psiMethod.getName() + ".http"), false);
        if (Objects.isNull(file)) {
            return null;
        }
        FileViewProvider fileViewProvider = factory.createFileViewProvider(file, HttpRequestLanguage.INSTANCE, PsiManager.getInstance(project), true);
        return new HttpRequestPsiFile(fileViewProvider);
    }


}
