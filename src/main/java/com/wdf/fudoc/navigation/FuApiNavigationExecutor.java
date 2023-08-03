package com.wdf.fudoc.navigation;

import com.google.common.collect.Lists;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.navigation.recent.ProjectRecentApi;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
import com.wdf.fudoc.util.FuApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-05-26 11:09:29
 */
@Slf4j
public class FuApiNavigationExecutor {

    private final Project project;
    private final ProjectRecentApi recentApi;

    private FuApiNavigationExecutor(Project project, ProjectRecentApi recentApi) {
        this.project = project;
        this.recentApi = recentApi;
    }

    public static FuApiNavigationExecutor getInstance(Project project, ProjectRecentApi recentApi) {
        return new FuApiNavigationExecutor(project, recentApi);
    }

    public List<ApiNavigationItem> getApiList() {
        return ApplicationManager.getApplication().runReadAction((Computable<List<ApiNavigationItem>>) () -> getApiList(GlobalSearchScope.allScope(project)));
    }


    /**
     * 获取所有的API
     */
    private List<ApiNavigationItem> getApiList(GlobalSearchScope globalSearchScope) {
        long start = System.currentTimeMillis();
        List<PsiClass> allController = FuApiUtils.findAllController(project, globalSearchScope);
        if (CollectionUtils.isEmpty(allController)) {
            return Lists.newArrayList();
        }
        log.info("读取【{}】个class. 共计耗时:{}ms", allController.size(), System.currentTimeMillis() - start);
        List<List<PsiClass>> partitionList = Lists.partition(allController, 50);
        List<CompletableFuture<List<ApiNavigationItem>>> completableFutureList = Lists.newArrayList();
        partitionList.forEach(f -> completableFutureList.add(CompletableFuture.supplyAsync(() -> ApplicationManager.getApplication().runReadAction((Computable<List<ApiNavigationItem>>) () -> readApi(f)))));
        List<ApiNavigationItem> apiList = completableFutureList.stream().flatMap(f -> f.join().stream()).collect(Collectors.toList());
        log.info("已读取【{}】条api. 共计耗时:{}ms", apiList.size(), System.currentTimeMillis() - start);
        return apiList;
    }


    private List<ApiNavigationItem> readApi(List<PsiClass> classList) {
        List<ApiNavigationItem> apiList = Lists.newArrayList();
        for (PsiClass psiClass : classList) {
            List<String> classUrlList = FuApiUtils.getClassUrl(psiClass);
            for (PsiMethod psiMethod : psiClass.getMethods()) {
                List<String> methodUrlList = Lists.newArrayList();
                RequestType requestType = FuApiUtils.getMethodUrl(psiMethod, methodUrlList);
                if (Objects.isNull(requestType) || CollectionUtils.isEmpty(methodUrlList)) {
                    continue;
                }
                List<String> urlList = FuApiUtils.joinUrl(classUrlList, methodUrlList);
                //获取title
                ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiMethod.getDocComment());
                String title = apiDocCommentData.getCommentTitle();
                //组装location
                String location = psiClass.getName() + "#" + psiMethod.getName();
                urlList.forEach(f -> {
                    ApiNavigationItem apiNavigationItem = new ApiNavigationItem(psiMethod, f, requestType, location, title);
                    //如果该api是之前访问过的 则添加到最近访问列表中
                    recentApi.initAdd(apiNavigationItem);
                    apiList.add(apiNavigationItem);
                });
            }
        }
        return apiList;
    }


}
