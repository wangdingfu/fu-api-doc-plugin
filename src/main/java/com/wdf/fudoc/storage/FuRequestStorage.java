package com.wdf.fudoc.storage;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.HttpRequestPsiUtils;
import com.intellij.httpClient.http.request.HttpRequestVariableSubstitutor;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.psi.HttpRequestBlock;
import com.intellij.httpClient.http.request.psi.HttpRequestTarget;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.dto.HttpRecentDTO;
import com.wdf.fudoc.request.http.impl.FuHttpClientImpl;
import com.wdf.fudoc.util.MatchUrlUtils;
import com.wdf.fudoc.util.StorageUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 【Fu Request】模块持久化
 * <p>
 * 持久化目录：${baseHome}/fudoc/${projectName}/${moduleName}/${controllerName}/${methodName}.http
 *
 * @author wangdingfu
 * @date 2023-05-21 23:26:16
 */
public class FuRequestStorage {


    public static FuHttpClient read(Project project, PsiClass psiClass) {
        return null;
    }

    /**
     * 读取HttpClient文件 并返回该对象
     *
     * @param psiClass  Controller
     * @param psiMethod 对应的接口方法体
     * @return 该接口对应http请求对象
     */
    public static FuHttpClient read(Project project, PsiClass psiClass, PsiMethod psiMethod, List<String> mappingUrlList) {
        if (CollectionUtils.isEmpty(mappingUrlList)) {
            return null;
        }
        //去指定目录下读取.http文件或则.rest文件
        HttpRequestPsiFile httpRequestPsiFile = StorageUtils.readHttp(project, psiClass, psiMethod);
        if (Objects.isNull(httpRequestPsiFile)) {
            return null;
        }
        HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(project, null);
        HttpRequestBlock[] requestBlocks = HttpRequestPsiUtils.getRequestBlocks(httpRequestPsiFile);
        for (HttpRequestBlock requestBlock : requestBlocks) {
            HttpRequest request = requestBlock.getRequest();
            HttpRequestTarget requestTarget = request.getRequestTarget();
            if (Objects.isNull(requestTarget)) {
                continue;
            }
            String httpPath = requestTarget.getHttpPath(substitutor);
            if (MatchUrlUtils.matchUrl(mappingUrlList, httpPath)) {
                //匹配成功 找到指定接口
                return new FuHttpClientImpl(project, request);
            }
        }
        return null;
    }


    /**
     * 读取最近一次请求记录
     *
     * @param project 当前项目
     * @return 最近一次请求记录 ${ControllerPath}#${methodName}#${mappingUrl}
     */
    public static HttpRecentDTO readRecent(Project project) {

        return null;
    }
}
