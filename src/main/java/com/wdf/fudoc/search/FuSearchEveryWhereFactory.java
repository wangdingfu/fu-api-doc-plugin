package com.wdf.fudoc.search;

import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributorFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.search.test.dto.FuApiNavigation;
import com.wdf.fudoc.search.test.FuApiSearchContributor;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2023-05-24 11:49:06
 */
public class FuSearchEveryWhereFactory implements SearchEverywhereContributorFactory<FuApiNavigation> {
    @Override
    public @NotNull SearchEverywhereContributor<FuApiNavigation> createContributor(@NotNull AnActionEvent initEvent) {
        return new FuApiSearchContributor(initEvent);
    }
}
