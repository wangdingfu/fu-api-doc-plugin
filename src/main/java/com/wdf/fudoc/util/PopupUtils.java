package com.wdf.fudoc.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.Computable;
import com.intellij.ui.ActiveComponent;
import com.intellij.ui.WindowMoveListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2022-09-04 21:23:19
 */
public class PopupUtils {

    public static final String FU_REQUEST_POPUP = "fudoc.request.popup";

    private final ComponentPopupBuilder componentPopupBuilder;

    public PopupUtils(ComponentPopupBuilder componentPopupBuilder) {
        this.componentPopupBuilder = componentPopupBuilder;
    }

    public static JBPopup create(JComponent mainComponent, JComponent focusComponent, AtomicBoolean pinStatus) {
        return getInstance(mainComponent, focusComponent).pin(pinStatus).create();
    }


    public static PopupUtils getInstance(JComponent mainComponent, JComponent focusComponent) {
        Project currProject = ProjectUtils.getCurrProject();
        // dialog 改成 popup, 第一个为根面板，第二个为焦点面板
        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(mainComponent, focusComponent);
        componentPopupBuilder.setProject(currProject)
                .setResizable(true)
                .setMovable(true)
                .setModalContext(false)
                .setRequestFocus(true)
                .setBelongsToGlobalPopupStack(true)
                .setDimensionServiceKey(currProject, FU_REQUEST_POPUP, true)
                .setLocateWithinScreenBounds(false)
                // 单击外部时取消弹窗
                .setCancelOnClickOutside(false)
                // 在其他窗口打开时取消
                .setCancelOnOtherWindowOpen(false)
                .setCancelOnWindowDeactivation(false);
        if (Objects.nonNull(focusComponent)) {
            addMouseListeners(mainComponent, focusComponent);
        }
        return new PopupUtils(componentPopupBuilder);
    }

    public JBPopup create() {
        JBPopup popup = this.componentPopupBuilder.createPopup();
        popup.showCenteredInCurrentWindow(ProjectUtils.getCurrProject());
        return popup;
    }


    public PopupUtils pin(AtomicBoolean pinStatus) {
        if (Objects.nonNull(pinStatus)) {
            // 鼠标点击外部时是否取消弹窗 外部单击, 未处于 pin 状态则可关闭
            this.componentPopupBuilder.setCancelOnMouseOutCallback(event -> event.getID() == MouseEvent.MOUSE_PRESSED && !pinStatus.get());
        }
        return this;
    }


    public static void addMouseListeners(JComponent rootComponent, JComponent focusComponent) {
        WindowMoveListener windowMoveListener = new WindowMoveListener(rootComponent);
        rootComponent.addMouseListener(windowMoveListener);
        rootComponent.addMouseMotionListener(windowMoveListener);
        focusComponent.addMouseListener(windowMoveListener);
        focusComponent.addMouseMotionListener(windowMoveListener);
    }

}
