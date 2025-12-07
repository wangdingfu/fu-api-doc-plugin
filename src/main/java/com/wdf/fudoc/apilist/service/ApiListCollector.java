package com.wdf.fudoc.apilist.service;

import com.google.common.collect.Lists;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apilist.pojo.ApiListItem;
import com.wdf.fudoc.util.FuApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * API 列表收集服务
 * 复用 FuApiNavigationExecutor 的逻辑,收集项目中所有的 API
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Slf4j
public class ApiListCollector {

    private final Project project;

    private ApiListCollector(Project project) {
        this.project = project;
    }

    public static ApiListCollector getInstance(Project project) {
        return new ApiListCollector(project);
    }

    /**
     * 收集项目中所有的 API
     *
     * @return API 列表
     */
    public List<ApiListItem> collectAllApis() {
        return ApplicationManager.getApplication().runReadAction((Computable<List<ApiListItem>>) () ->
                collectApis(GlobalSearchScope.projectScope(project))
        );
    }

    /**
     * 收集指定范围内的 API
     */
    private List<ApiListItem> collectApis(GlobalSearchScope scope) {
        long start = System.currentTimeMillis();

        // 1. 查找所有的 Controller 类
        List<PsiClass> allController = FuApiUtils.findAllController(project, scope);
        if (CollectionUtils.isEmpty(allController)) {
            log.info("未找到任何 Controller 类");
            return Lists.newArrayList();
        }

        log.info("找到 {} 个 Controller 类", allController.size());

        // 2. 分批处理 Controller 类,避免单次处理过多
        List<List<PsiClass>> partitionList = Lists.partition(allController, 50);
        List<CompletableFuture<List<ApiListItem>>> futures = Lists.newArrayList();

        partitionList.forEach(partition ->
                futures.add(CompletableFuture.supplyAsync(() ->
                        ApplicationManager.getApplication().runReadAction((Computable<List<ApiListItem>>) () ->
                                readApiFromClasses(partition)
                        )
                ))
        );

        // 3. 等待所有任务完成并合并结果
        List<ApiListItem> apiList = futures.stream()
                .flatMap(f -> f.join().stream())
                .collect(Collectors.toList());

        log.info("收集到 {} 个 API,耗时 {}ms", apiList.size(), System.currentTimeMillis() - start);
        return apiList;
    }

    /**
     * 从 Controller 类列表中读取 API
     */
    private List<ApiListItem> readApiFromClasses(List<PsiClass> classList) {
        List<ApiListItem> apiList = Lists.newArrayList();

        for (PsiClass psiClass : classList) {
            try {
                // 获取类级别的 URL 前缀
                List<String> classUrlList = FuApiUtils.getClassUrl(psiClass);

                // 获取类所属的 Module
                Module module = ModuleUtil.findModuleForPsiElement(psiClass);
                String moduleName = module != null ? module.getName() : "Unknown";

                // 获取类的全限定名
                String className = psiClass.getQualifiedName();
                if (className == null) {
                    className = psiClass.getName();
                }

                // 遍历类中的所有方法
                for (PsiMethod psiMethod : psiClass.getMethods()) {
                    ApiListItem apiItem = readApiFromMethod(psiMethod, classUrlList, moduleName, className);
                    if (apiItem != null) {
                        apiList.add(apiItem);
                    }
                }
            } catch (Exception e) {
                log.warn("读取 Controller 类 {} 的 API 时出错", psiClass.getName(), e);
            }
        }

        return apiList;
    }

    /**
     * 从方法中读取 API 信息
     */
    private ApiListItem readApiFromMethod(PsiMethod psiMethod,
                                          List<String> classUrlList,
                                          String moduleName,
                                          String className) {
        try {
            // 获取方法级别的 URL 和请求类型
            List<String> methodUrlList = Lists.newArrayList();
            RequestType requestType = FuApiUtils.getMethodUrl(psiMethod, methodUrlList);

            // 如果没有找到请求类型或 URL,跳过
            if (Objects.isNull(requestType) || CollectionUtils.isEmpty(methodUrlList)) {
                return null;
            }

            // 拼接完整的 URL
            List<String> fullUrlList = FuApiUtils.joinUrl(classUrlList, methodUrlList);
            if (CollectionUtils.isEmpty(fullUrlList)) {
                return null;
            }

            // 使用第一个 URL (通常只有一个)
            String url = fullUrlList.get(0);

            // 解析方法注释,获取 API 标题
            ApiDocCommentData commentData = DocCommentParseHelper.parseComment(psiMethod);
            String title = commentData.getCommentTitle();
            if (FuStringUtils.isBlank(title)) {
                title = psiMethod.getName();
            }

            // 创建 API 列表项
            return new ApiListItem(psiMethod, url, requestType, title, moduleName, className);

        } catch (Exception e) {
            log.warn("读取方法 {} 的 API 信息时出错", psiMethod.getName(), e);
            return null;
        }
    }
}
