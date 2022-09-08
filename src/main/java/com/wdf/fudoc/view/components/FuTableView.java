package com.wdf.fudoc.view.components;

import com.intellij.ui.table.JBTable;
import lombok.Setter;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-07 16:36:41
 */
public class FuTableView extends JBTable {


    @Setter
    private FuTableCellEditorListener fuTableCellEditorListener;

    public FuTableView(TableModel model) {
        super(model);
    }


    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (Objects.nonNull(fuTableCellEditorListener)) {
            TableCellEditor cellEditor = fuTableCellEditorListener.getCellEditor(this, row, column);
            if (Objects.nonNull(cellEditor)) {
                return cellEditor;
            }
        }
        return super.getCellEditor(row, column);
    }
}
