package com.wdf.fudoc.navigation;

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.search.test.FuApiSearchContributor;
import com.wdf.fudoc.search.test.dto.FuApiNavigation;
import org.jetbrains.annotations.NotNull;

/**
 * 全局搜索工厂类
 *
 * @author wangdingfu
 * @date 2023-05-24 11:49:06
 */
public class FuNavigationApiEveryWhereFactory implements SearchEverywhereContributorFactory<ApiNavigationItem> {

    @Override
    public @NotNull SearchEverywhereContributor<ApiNavigationItem> createContributor(@NotNull AnActionEvent initEvent) {
        return new FuApiNavigationContributor(initEvent);
    }
}
