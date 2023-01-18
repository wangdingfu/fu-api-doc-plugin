package com.wdf.fudoc.components;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author wangdingfu
 * @date 2022-09-07 18:32:02
 */
public abstract class SingleAction extends AnAction implements Toggleable {

    public SingleAction(@Nullable @NlsActions.ActionText final String text,
                        @Nullable @NlsActions.ActionDescription final String description,
                        @Nullable final Icon icon) {
        super(text, description, icon);
    }


    @Override
    public final void actionPerformed(@NotNull final AnActionEvent e) {
        if(!isSelected(e)){
            //只有选中时才触发选中按钮 如果已经选中了则不触发选中事件
            setSelected(e);
            final Presentation presentation = e.getPresentation();
            Toggleable.setSelected(presentation, true);
        }
    }

    /**
     * Returns the selected (checked, pressed) state of the action.
     *
     * @param e the action event representing the place and context in which the selected state is queried.
     * @return true if the action is selected, false otherwise
     */
    public abstract boolean isSelected(@NotNull AnActionEvent e);

    /**
     * Sets the selected state of the action to the specified value.
     *
     * @param e     the action event which caused the state change.
     */
    public abstract void setSelected(@NotNull AnActionEvent e);

    @Override
    public void update(@NotNull final AnActionEvent e) {
        boolean selected = isSelected(e);
        final Presentation presentation = e.getPresentation();
        Toggleable.setSelected(presentation, selected);
    }
}
