package com.wdf.fudoc.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.api.util.JsonUtil;
import com.wdf.fudoc.request.http.FuRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

//
//    /**
//     * 读取http文件
//     *
//     * @param project   项目
//     * @param psiClass  controller
//     * @param psiMethod 接口方法体
//     * @return http文件内容
//     */
//    public static HttpRequestPsiFile readHttp(Project project, PsiClass psiClass, PsiMethod psiMethod) {
//        final FileViewProviderFactory factory = LanguageFileViewProviders.INSTANCE.forLanguage(HttpRequestLanguage.INSTANCE);
//        String requestPath = FuRequestUtils.getRequestPath(project, psiClass);
//        VirtualFile file = VfsUtil.findFile(Paths.get(requestPath, psiMethod.getName() + ".http"), false);
//        if (Objects.isNull(file)) {
//            return null;
//        }
//        FileViewProvider fileViewProvider = factory.createFileViewProvider(file, HttpRequestLanguage.INSTANCE, PsiManager.getInstance(project), true);
//        return new HttpRequestPsiFile(fileViewProvider);
//    }

    public static void writeJson(String path, String fileName, Object value) {
        write(path, fileName, JsonUtil.toJson(value));
    }

    /**
     * 将json数据写入到指定目录下
     *
     * @param path     目录
     * @param fileName 文件名称
     * @param value    json数据
     */
    public static void write(String path, String fileName, String value) {
        //将数据格式化为json 并写入硬盘
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                //获取或则创建目录
                VirtualFile virtualFile = VfsUtil.createDirectoryIfMissing(path);
                if (Objects.isNull(virtualFile)) {
                    log.info("持久化目录【{}】不存在", path);
                    return;
                }
                //读取该文件
                VirtualFile httpVirtualFile = virtualFile.findOrCreateChildData(null, fileName);
                httpVirtualFile.setCharset(StandardCharsets.UTF_8);
                VfsUtil.saveText(httpVirtualFile, value);
            } catch (Exception e) {
                log.info("持久化{}文件异常", fileName, e);
            }
        });
    }


    public static String readContent(String path, String fileName) {
        VirtualFile file = ApplicationManager.getApplication().runReadAction((Computable<VirtualFile>) () -> VfsUtil.findFile(Paths.get(path, fileName), false));
        if (Objects.nonNull(file) && file.exists()) {
            try {
                return StringUtils.toEncodedString(file.contentsToByteArray(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.info("读取文件【{}】异常", file.getPath());
            }
        }
        return StringUtils.EMPTY;
    }


}
