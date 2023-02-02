package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.fudoc.components.message.FuMsgBuilder;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:39:54
 */
public class TestTableAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FuMessageComponent fuMessageComponent = new FuMessageComponent();
        fuMessageComponent.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, fuMessageComponent);
        fuMessageComponent.setMsg(FuMsgBuilder.getInstance().text("Size: ").text("806 B", FuColor.GREEN).build());
        PopupUtils.create(fuMessageComponent,fuMessageComponent,new AtomicBoolean(false));
    }
}
