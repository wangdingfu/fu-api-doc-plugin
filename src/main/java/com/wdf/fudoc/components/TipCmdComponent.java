package com.wdf.fudoc.components;

import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBList;
import com.wdf.fudoc.components.bo.TipCmd;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-05-31 17:03:40
 */
public class TipCmdComponent extends DefaultListModel<TipCmd> {

    @Getter
    private final JBList<TipCmd> list;

    public TipCmdComponent(List<TipCmd> tipCmdList) {
        if (CollectionUtils.isNotEmpty(tipCmdList)) {
            addAll(tipCmdList);
        }
        this.list = new JBList<>(this);
        //设置列表如何渲染
        this.list.setCellRenderer(new CmdCellRenderer());
        this.list.addListSelectionListener(e -> {
            TipCmd selectedValue = this.list.getSelectedValue();
            System.out.println("当前点击了:" + selectedValue.getText() + "; 需要执行的命令是:" + selectedValue.getCmd());
        });
    }


    public static class CmdCellRenderer extends JLabel implements ListCellRenderer<TipCmd>, MouseMotionListener {
        public CmdCellRenderer() {
            setOpaque(true);
            addMouseMotionListener(this);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends TipCmd> list, TipCmd value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getText());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground()); // 设置背景颜色
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground()); // 设置前景颜色
            setFont(list.getFont()); // 设置字体样式
            return this;
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}
