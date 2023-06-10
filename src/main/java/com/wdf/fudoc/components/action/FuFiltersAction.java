package com.wdf.fudoc.components.action;

import com.intellij.ide.util.ElementsChooser;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.wdf.fudoc.components.listener.FuFiltersListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FuFiltersAction<T> extends ShowFilterAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    final Runnable rebuildRunnable;

    private final FuFiltersListener<T> filter;


    public FuFiltersAction(String text, FuFiltersListener<T> fuFiltersListener, @NotNull Runnable rebuildRunnable) {
        super(text, text, fuFiltersListener.getElementIcon(null));
        this.filter = fuFiltersListener;
        this.rebuildRunnable = rebuildRunnable;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean isActive() {
        return filter.getAllElements().size() != filter.getSelectedElements().size();
    }

    @Override
    protected ElementsChooser<?> createChooser() {
        return createChooser(filter, rebuildRunnable);
    }

    private static <T> ElementsChooser<T> createChooser(FuFiltersListener<T> filter, @NotNull Runnable rebuildRunnable) {
        ElementsChooser<T> res = new ElementsChooser<>(filter.getAllElements(), false) {
            @Override
            protected String getItemText(@NotNull T value) {
                return filter.getElementText(value);
            }

            @Nullable
            @Override
            protected Icon getItemIcon(@NotNull T value) {
                return filter.getElementIcon(value);
            }
        };
        res.markElements(filter.getSelectedElements());
        ElementsChooser.ElementsMarkListener<T> listener = (element, isMarked) -> {
            filter.setSelected(element, isMarked);
            rebuildRunnable.run();
        };
        res.addElementsMarkListener(listener);
        return res;
    }


    public void exit() {
        if (myFilterPopup != null && !myFilterPopup.isDisposed()) {
            myFilterPopup.cancel();
        }
    }

}