package com.wdf.fudoc.test.action;

import com.intellij.httpClient.converters.RequestBuilder;
import com.intellij.httpClient.execution.HttpRequestConfig;
import com.intellij.httpClient.execution.RestClientFormBodyPart;
import com.intellij.httpClient.execution.RestClientRequest;
import com.intellij.httpClient.execution.RestClientRequestBuilder;
import com.intellij.httpClient.http.request.HttpRequestPsiConverter;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.HttpRequestPsiUtils;
import com.intellij.httpClient.http.request.HttpRequestVariableSubstitutor;
import com.intellij.httpClient.http.request.psi.HttpQuery;
import com.intellij.httpClient.http.request.psi.HttpQueryParameter;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.psi.HttpRequestTarget;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.navigation.FuApiNavigationExecutor;
import com.wdf.fudoc.navigation.recent.ProjectRecentApi;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
import com.wdf.fudoc.util.FuRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 获取当前项目
        Project project = e.getProject();

        // 获取 Maven 项目管理器
        MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(project);

        // 获取 Maven 项目
        MavenProject mavenProject = mavenProjectsManager.getProjects().get(0);

        // 获取 Maven Model
        Collection<String> profilesIds = mavenProject.getProfilesIds();

//        AuthSettingView authSettingView = new AuthSettingView(e.getProject());
//        PopupUtils.create(authSettingView.getRootPanel(),null,new AtomicBoolean(true));
//        request(e);
//        apiTest(e);
    }



    private void apiTest(AnActionEvent e) {
        ProjectRecentApi projectRecentApi = RecentNavigationManager.create(e.getProject());
        long start = System.currentTimeMillis();
        List<ApiNavigationItem> apiList = FuApiNavigationExecutor.getInstance(e.getProject(), projectRecentApi).getApiList();
        System.out.println("读取api【" + apiList.size() + "】条 共计耗时:" + (System.currentTimeMillis() - start) + "ms");
    }


    private void request(AnActionEvent e) {
        HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(e.getProject());
        //读取http文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        HttpRequest httpRequest = FuRequestUtils.getHttpRequest((HttpRequestPsiFile) psiFile, e.getData(LangDataKeys.EDITOR));

        if (Objects.isNull(psiFile)) {
            return;
        }
        HttpRequest firstRequest = HttpRequestPsiUtils.getFirstRequest(psiFile);
        if (Objects.isNull(firstRequest)) {
            return;
        }
        HttpRequestTarget requestTarget = firstRequest.getRequestTarget();
        String httpUrl = firstRequest.getHttpUrl(substitutor);
        log.info("httpUrl:" + httpUrl);
        String httpMethod = firstRequest.getHttpMethod();
        log.info("httpMethod:" + httpMethod);
        String text = firstRequest.getText();
        log.info("text:" + text);
        HttpQuery query;
        if (Objects.nonNull(requestTarget) && Objects.nonNull(query = requestTarget.getQuery())) {
            List<HttpQueryParameter> queryParameterList = query.getQueryParameterList();
            for (HttpQueryParameter httpQueryParameter : queryParameterList) {
                String value = httpQueryParameter.getValue(substitutor);
                String key = httpQueryParameter.getKey(substitutor);
                log.info("key:" + key + "=" + value);
            }
        }
        RequestBuilder<RestClientRequest, RestClientFormBodyPart> requestBuilder = new RestClientRequestBuilder();
        HttpRequestConfig requestConfig = HttpRequestPsiConverter.toRequestConfig(firstRequest);
//        try {
//            RestClientRequest restClientRequest = HttpRequestPsiConverter.convertFromHttpRequest(firstRequest, substitutor, requestBuilder);
//            CurlCopyPastePreProcessor preProcessor = new CurlCopyPastePreProcessor();
//            HttpRequestCopyAsCurlAction httpRequestCopyAsCurlAction = new HttpRequestCopyAsCurlAction();
//            List<File> files = restClientRequest.getFiles();
//            String url = restClientRequest.getURL();
//            String textToSend = restClientRequest.getTextToSend();
//        } catch (Exception exception) {
//            log.info("解析http请求错误", exception);
//        }
    }


}
