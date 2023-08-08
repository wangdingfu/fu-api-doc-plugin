package com.wdf.fudoc.futool.beancopy.template;

import com.intellij.codeInsight.template.CustomLiveTemplateBase;
import com.intellij.codeInsight.template.CustomTemplateCallback;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2023-08-08 18:17:03
 */
public class FuDocLiveTemplate extends CustomLiveTemplateBase {



    @Override
    public @Nullable String computeTemplateKey(@NotNull CustomTemplateCallback callback) {
        return null;
    }

    @Override
    public boolean isApplicable(@NotNull CustomTemplateCallback callback, int offset, boolean wrapping) {
        return false;
    }

    @Override
    public boolean supportsWrapping() {
        return false;
    }

    @Override
    public void expand(@NotNull String key, @NotNull CustomTemplateCallback callback) {

    }

    @Override
    public void wrap(@NotNull String selection, @NotNull CustomTemplateCallback callback) {

    }

    @Override
    public @NotNull @NlsActions.ActionText String getTitle() {
        return null;
    }

    @Override
    public char getShortcut() {
        return 0;
    }
}
