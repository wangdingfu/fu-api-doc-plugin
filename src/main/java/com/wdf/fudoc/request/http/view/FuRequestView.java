package com.wdf.fudoc.request.http.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.DialogWrapperDialog;
import com.wdf.fudoc.request.http.FuRequest;
import com.wdf.fudoc.request.view.HttpDialogView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-05-27 20:10:49
 */
public class FuRequestView extends DialogWrapper {

    private final FuRequest fuRequest;

    protected FuRequestView(@Nullable Project project, boolean canBeParent, FuRequest fuRequest) {
        super(project, canBeParent);
        this.fuRequest = fuRequest;
    }


    public static FuRequestView getInstance(Project project, FuRequest fuRequest) {
        return new FuRequestView(project, true, fuRequest);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return null;
    }

}
