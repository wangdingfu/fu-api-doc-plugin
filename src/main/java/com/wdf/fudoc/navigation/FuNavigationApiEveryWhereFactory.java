package com.wdf.fudoc.navigation;

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.search.GlobalSearchScope;
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
