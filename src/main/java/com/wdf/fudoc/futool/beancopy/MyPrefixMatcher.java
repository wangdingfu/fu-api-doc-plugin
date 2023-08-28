package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.completion.PrefixMatcher;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2023-08-08 13:41:56
 */
public class MyPrefixMatcher extends PrefixMatcher {
    protected MyPrefixMatcher(String prefix) {
        super(prefix);
    }

    @Override
    public boolean prefixMatches(@NotNull String name) {
        return name.equalsIgnoreCase(myPrefix);
    }

    @Override
    public @NotNull PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
        return new MyPrefixMatcher(prefix);
    }
}
