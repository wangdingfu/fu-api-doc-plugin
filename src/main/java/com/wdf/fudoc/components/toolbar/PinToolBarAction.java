package com.wdf.fudoc.components.toolbar;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2022-09-17 18:50:14
 */
public class PinToolBarAction extends ToggleAction {

    @Getter
    public static final AtomicBoolean pinStatus = new AtomicBoolean(false);

    @Override
    public boolean isDumbAware() {
        return true;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return pinStatus.get();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        pinStatus.set(state);
    }
}
