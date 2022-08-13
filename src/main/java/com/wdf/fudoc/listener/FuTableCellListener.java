package com.wdf.fudoc.listener;

import com.wdf.fudoc.common.FuTableCellChangeNotifyListener;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * swing中针对JTable的单元格事件监听对象
 *
 * @author wangdingfu
 * @date 2022-08-13 17:29:21
 */
@Getter
public class FuTableCellListener implements PropertyChangeListener, Runnable {


    private final JTable jTable;

    private final FuTableCellChangeNotifyListener notifyListener;

    private int row;
    private int column;
    private String beforeValue;
    private String currentValue;

    public FuTableCellListener(JTable jTable, FuTableCellChangeNotifyListener notifyListener) {
        this.jTable = jTable;
        this.notifyListener = notifyListener;
    }

    /**
     * 当属性有变更时会调用该方法
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (jTable.isEditing()) {
            //开始编辑
            processEditingStarted();
        } else {
            //停止编辑
            processEditingStopped();
        }
    }

    /**
     * 当开始编辑时. 会调用该方法将编辑之前的值保存下来
     */
    @Override
    public void run() {
        row = jTable.convertRowIndexToModel(jTable.getEditingRow());
        column = jTable.convertColumnIndexToModel(jTable.getEditingColumn());
        Object valueAt = jTable.getModel().getValueAt(row, column);
        beforeValue = Objects.isNull(valueAt) ? StringUtils.EMPTY : valueAt.toString();
        currentValue = null;
    }


    /**
     * 开始编辑前调用 主要为了保存编辑之前的值
     */
    private void processEditingStarted() {
        // the invokeLater is necessary because the editing row and editing
        // column of the table have not been set when the "tableCellEditor"
        // propertyChangeEvent is fired. this results in the "run" method being invoked
        SwingUtilities.invokeLater(this);
    }


    /**
     * 编辑完成后调用 保存编辑之后的值 并调用传入的事件对象 将结果传递出去
     */
    private void processEditingStopped() {
        Object valueAt = jTable.getModel().getValueAt(row, column);
        currentValue = Objects.isNull(valueAt) ? StringUtils.EMPTY : valueAt.toString();
        // the data has changed, invoke the supplied action
        if (!currentValue.equals(beforeValue)) {
            // make a copy of the data in case another cell starts editing
            notifyListener.notify(this.row, this.column, this.beforeValue, this.currentValue);
        }
    }
}
