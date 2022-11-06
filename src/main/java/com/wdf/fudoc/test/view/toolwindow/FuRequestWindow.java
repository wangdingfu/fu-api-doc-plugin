package com.wdf.fudoc.test.view.toolwindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.wdf.fudoc.test.view.TestRequestFrom;
import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-08-25 22:07:47
 */
public class FuRequestWindow extends SimpleToolWindowPanel implements DataProvider {

    @Getter
    private final JPanel rootPanel;

    private Project project;


    public FuRequestWindow(@NotNull Project project) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.project = project;
        this.rootPanel = new TestRequestFrom(project).getRootPanel();
        setContent(this.rootPanel);
    }


    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if ("FuRequestWindow".equals(dataId)) {
            return this;
        }
        return super.getData(dataId);
    }
}
