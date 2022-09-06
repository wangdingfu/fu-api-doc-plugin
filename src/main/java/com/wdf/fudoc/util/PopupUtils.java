package com.wdf.fudoc.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * @author wangdingfu
 * @date 2022-09-04 21:23:19
 */
public class PopupUtils {

    public static final String FU_REQUEST_POPUP = "fudoc.request.popup";

    public static JBPopup popup(JComponent component) {
        Project currProject = ProjectUtils.getCurrProject();
        // dialog 改成 popup, 第一个为根面板，第二个为焦点面板
        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(component, null)
                .setProject(currProject)
                .setResizable(true)
                .setMovable(true)

                .setModalContext(false)
                .setRequestFocus(true)
                .setBelongsToGlobalPopupStack(true)
                .setDimensionServiceKey(null, FU_REQUEST_POPUP, true)
                .setLocateWithinScreenBounds(false)
                // 鼠标点击外部时是否取消弹窗 外部单击, 未处于 pin 状态则可关闭
                .setCancelOnMouseOutCallback(event -> event.getID() == MouseEvent.MOUSE_PRESSED)

                // 单击外部时取消弹窗
                .setCancelOnClickOutside(false)
                // 在其他窗口打开时取消
                .setCancelOnOtherWindowOpen(false)
                .setCancelOnWindowDeactivation(false)
                .createPopup();
        popup.showCenteredInCurrentWindow(currProject);
        return popup;
    }

}
