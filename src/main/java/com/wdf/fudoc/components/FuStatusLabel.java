package com.wdf.fudoc.components;

import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-09 22:51:14
 */
public class FuStatusLabel {

    @Getter
    private final JLabel label;

    private final FuStatusLabelListener listener;

    public FuStatusLabel(String text, Icon icon, FuStatusLabelListener listener) {
        this.label = new JLabel(icon, SwingConstants.LEFT);
        this.label.setBorder(JBUI.Borders.empty(0, 10));
        this.listener = listener;
        this.label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setText(text);
        initLabel();
    }

    public void setText(String text) {
        this.label.setText(Objects.isNull(text) ? StringUtils.EMPTY : text);
        this.label.setEnabled(StringUtils.isNotBlank(text));
    }


    public String getText() {
        return this.label.getText();
    }


    private void initLabel() {
        this.label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu popupMenu = buildPopupMenu();
                if (Objects.nonNull(popupMenu)) {
                    popupMenu.show(label, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //鼠标滑入时 设置背景色
                label.setOpaque(true);
                label.setBackground(JBUI.CurrentTheme.StatusBar.Widget.HOVER_BACKGROUND);
                label.setForeground(JBUI.CurrentTheme.StatusBar.Widget.HOVER_FOREGROUND);
            }


            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标滑出时，恢复背景色和边框
                label.setOpaque(false);
                label.setBackground(JBUI.CurrentTheme.StatusBar.BACKGROUND);
                label.setForeground(JBUI.CurrentTheme.StatusBar.Widget.FOREGROUND);
            }
        });
    }


    private JPopupMenu buildPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        List<BasePopupMenuItem> dataList = listener.getList();
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        dataList.forEach(f -> popupMenu.add(buildMenuItem(f)));
        return popupMenu;
    }


    private JMenuItem buildMenuItem(BasePopupMenuItem item) {
        JMenuItem menuItem = new JMenuItem(item.getShowName());
        menuItem.setIcon(item.getIcon());
        menuItem.addActionListener(e -> {
            listener.select(item.getShowName());
            if (item.isCanSelect()) {
                label.setText(item.getShowName());
            }
        });
        return menuItem;
    }
}
