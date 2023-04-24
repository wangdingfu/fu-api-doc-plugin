package com.wdf.fudoc.components;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

/**
 * @author wangdingfu
 * @date 2023-01-19 15:38:31
 */
public class FuButtonTableEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {


    private JPanel panel;

    private JButton button;


    public FuButtonTableEditor() {
        initButton();
        initPanel();
    }

    @Override
    public Object getCellEditorValue() {
        return this.button.getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    private void initButton() {
        this.button = new JButton();
        this.button.setText("重新同步");

        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 触发取消编辑的事件，不会调用tableModel的setValue方法。
                FuButtonTableEditor.this.fireEditingCanceled();

                // 这里可以做其它操作。
                // 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。
            }
        });

    }

    private void initPanel() {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }



    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return this.panel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.panel;
    }
}
