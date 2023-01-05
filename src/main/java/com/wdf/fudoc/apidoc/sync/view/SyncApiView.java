package com.wdf.fudoc.apidoc.sync.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.components.FuTableComponent;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * 同步接口至第三方接口文档视图
 *
 * @author wangdingfu
 * @date 2023-01-01 18:17:57
 */
public class SyncApiView extends DialogWrapper {

    @Getter
    private List<SyncApiTableData> tableDataList;


    public FuTableComponent<SyncApiTableData> fuTableComponent;


    public SyncApiView(@Nullable Project project) {
        super(project, true);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return null;
    }
}
