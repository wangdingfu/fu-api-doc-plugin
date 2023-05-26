package com.wdf.fudoc.navigation;

import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.ide.actions.searcheverywhere.WeightedSearchEverywhereContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.util.Processor;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.containers.ContainerUtil;
import com.wdf.fudoc.navigation.recent.ProjectRecentApi;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
import com.wdf.fudoc.navigation.match.FuApiMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Api导航实现
 *
 * @author wangdingfu
 * @date 2023-05-25 18:51:03
 */
@Slf4j
public class FuApiNavigationContributor implements WeightedSearchEverywhereContributor<ApiNavigationItem> {

    public static final String INSTANCE = "Fu Api";

    private final Project project;
    private final ProjectRecentApi projectRecentApi;
    /**
     * api匹配器
     */
    private final FuApiMatcher fuApiMatcher;

    public FuApiNavigationContributor(AnActionEvent initEvent) {
        this.project = initEvent.getProject();
        projectRecentApi = RecentNavigationManager.create(project);
        this.fuApiMatcher = new FuApiMatcher(project, FuApiNavigationExecutor.getInstance(project, projectRecentApi).getApiList());
    }


    /**
     * 搜索API
     *
     * @param pattern           搜索框输入的API
     * @param progressIndicator 调度器
     * @param consumer          处理器（当url匹配上需要交割处理器添加到结果列表中）
     */
    @Override
    public void fetchWeightedElements(@NotNull String pattern, @NotNull ProgressIndicator progressIndicator, @NotNull Processor<? super FoundItemDescriptor<ApiNavigationItem>> consumer) {
        long start = System.currentTimeMillis();
        log.info("搜索api.....");
        List<FoundItemDescriptor<ApiNavigationItem>> recentApiList;
        if (StringUtils.isBlank(pattern) && CollectionUtils.isNotEmpty(recentApiList = projectRecentApi.historyList())) {
            //直接展示最近搜索的api
            ContainerUtil.process(recentApiList, consumer);
            log.info("匹配历史搜索的api【{}】条. 共计耗时:{}ms", recentApiList.size(), System.currentTimeMillis() - start);
            return;
        }
        boolean flag = fuApiMatcher.matchApi(pattern, progressIndicator, consumer);
        log.info("匹配 api【{}】{}. 共计耗时:{}ms", pattern, flag, System.currentTimeMillis() - start);
    }


    /**
     * 提供方ID
     */
    @Override
    public @NotNull String getSearchProviderId() {
        return INSTANCE;
    }

    @Override
    public @NotNull @Nls String getGroupName() {
        return INSTANCE;
    }

    @Override
    public int getSortWeight() {
        return 800;
    }

    @Override
    public boolean showInFindResults() {
        return false;
    }

    /**
     * 当选中API时处理事件===》导航到具体代码
     *
     * @param selected   item chosen by user
     * @param modifiers  keyboard modifiers
     * @param searchText text from search field
     */
    @Override
    public boolean processSelectedItem(@NotNull ApiNavigationItem selected, int modifiers, @NotNull String searchText) {
        PsiNavigateUtil.navigate(selected.getPsiElement());
        //记录历史搜索
        ApplicationManager.getApplication().runReadAction(() -> RecentNavigationManager.add(project, selected));
        return true;
    }

    @Override
    public boolean isEmptyPatternSupported() {
        return true;
    }

    @Override
    public boolean isShownInSeparateTab() {
        return true;
    }

    /**
     * 渲染API
     */
    @Override
    public @NotNull ListCellRenderer<? super ApiNavigationItem> getElementsRenderer() {
        return new FuApiRenderer(this);
    }

    @Override
    public @Nullable @Nls String getAdvertisement() {
        return DumbService.isDumb(project) ? "The project is being indexed..." : "type url to search";
    }


    @Override
    public @Nullable Object getDataForItem(@NotNull ApiNavigationItem element, @NotNull String dataId) {
        return null;
    }
}
