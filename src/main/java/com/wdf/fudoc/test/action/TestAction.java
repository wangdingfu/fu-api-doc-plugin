package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.*;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FuTableComponent<YApiProjectTableData> fuTableComponent = FuTableComponent.create(FuTableColumnFactory.yapi(), YApiProjectTableData.class);
        PopupUtils.create(fuTableComponent.createPanel(),null,new AtomicBoolean(true));
    }


}
