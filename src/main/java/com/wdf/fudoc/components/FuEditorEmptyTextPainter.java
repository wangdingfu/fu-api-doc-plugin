package com.wdf.fudoc.components;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.ActivateToolWindowAction;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.MacKeymapUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


/**
 * @author wangdingfu
 * @date 2023-05-31 14:35:09
 */
public class FuEditorEmptyTextPainter {


    public void paintEmptyText(@NotNull JComponent splitters, @NotNull Graphics g) {
        UISettings.setupAntialiasing(g);
        UIUtil.TextPainter painter = createTextPainter();
        advertiseActions(painter);
        painter.draw(g, (width, height) -> {
            Dimension s = splitters.getSize();
            int w = (s.width - width) / 2;
            int h = (int) (s.height * heightRatio());
            return Couple.of(w, h);
        });
    }

    protected double heightRatio() {
        return 0.375;
    }

    protected void advertiseActions(@NotNull UIUtil.TextPainter painter) {
        appendLine(painter, "1、配置http请求获取接口鉴权信息");
        appendLine(painter, "2、编写JS脚本将鉴权信息设置到全局请求头或参数中");
        appendAction(painter, "Fu Request", "Alt+R");
        appendAction(painter, "Search Api", getActionShortcutText("fu.api.navigation"));
    }

    @NotNull
    protected String getActionShortcutText(@NonNls @NotNull String actionId) {
        return KeymapUtil.getFirstKeyboardShortcutText(actionId);
    }

    protected void appendAction(@NotNull UIUtil.TextPainter painter, @NotNull @Nls String action, @Nullable String shortcut) {
        if (StringUtil.isEmpty(shortcut)) return;
        appendLine(painter, action + " " + "<shortcut>" + shortcut + "</shortcut>");
    }

    protected void appendLine(@NotNull UIUtil.TextPainter painter, @NotNull String line) {
        painter.appendLine(line);
    }


    @NotNull
    public static UIUtil.TextPainter createTextPainter() {
        return new UIUtil.TextPainter()
                .withLineSpacing(1.8f)
                .withColor(JBColor.namedColor("Editor.foreground", new JBColor(Gray._80, Gray._160)))
                .withFont(JBUI.Fonts.label(16f));
    }
}
