package com.wdf.fudoc.components;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2023-01-19 16:38:11
 */
public class ButtonTableCellEditor extends DefaultCellEditor {
    protected JButton button;
    private Integer row;

    @Getter
    private final TableCellRenderer cellRenderer;

    public ButtonTableCellEditor(String btnName, Function<Integer, String> function) {
        super(new JCheckBox());
        button = new JButton();
        button.setOpaque(true);
        button.setText(btnName);
        button.addActionListener(e -> {
            if (Objects.nonNull(function) && Objects.nonNull(row)) {
                //将按钮置为同步中...
                button.setText("同步中...");
                button.setEnabled(false);
                String result = function.apply(row);
                //将按钮置为已同步
                button.setText(result);
                button.setEnabled(true);
            }
            fireEditingStopped();
        });
        this.cellRenderer = new FuButtonRenderer(this.button);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        Object valueAt = table.getModel().getValueAt(row, column);
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }

}
