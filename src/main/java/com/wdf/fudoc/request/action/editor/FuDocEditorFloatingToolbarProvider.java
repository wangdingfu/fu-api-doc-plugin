package com.wdf.fudoc.request.action.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-08 21:06:45
 */
public class FuDocEditorFloatingToolbarProvider extends AbstractFloatingToolbarProvider {
    public FuDocEditorFloatingToolbarProvider() {
        super("fu.doc.editor.format");
    }

    @Override
    public void register(@NotNull FloatingToolbarComponent component, @NotNull Disposable parentDisposable) {
        super.register(component, parentDisposable);
        component.scheduleShow();
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }
}
