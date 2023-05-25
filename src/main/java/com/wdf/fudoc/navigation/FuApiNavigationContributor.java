package com.wdf.fudoc.navigation;

import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.ide.actions.searcheverywhere.WeightedSearchEverywhereContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.util.Processor;
import com.intellij.util.PsiNavigateUtil;
import com.wdf.fudoc.navigation.match.FuApiMatcher;
import com.wdf.fudoc.search.test.FuSearchApiExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Api导航实现
 *
 * @author wangdingfu
 * @date 2023-05-25 18:51:03
 */
@Slf4j
public class FuApiNavigationContributor implements WeightedSearchEverywhereContributor<ApiNavigationItem> {

    public static final String INSTANCE = "FuApi";

    private final Project project;
    private final FuApiMatcher fuApiMatcher;

    public FuApiNavigationContributor(AnActionEvent initEvent) {
        this.project = initEvent.getProject();
        this.fuApiMatcher = new FuApiMatcher(project, FuSearchApiExecutor.getInstance(project).getApiList());
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
        boolean b = fuApiMatcher.matchApi(pattern, progressIndicator, consumer);
        log.info("api导航:{}", b);
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
