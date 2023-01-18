package com.wdf.fudoc.components;

import com.intellij.ui.table.JBTable;
import com.wdf.fudoc.components.listener.FuTableListener;
import lombok.Setter;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-07 16:36:41
 */
public class FuTableView<T> extends JBTable {


    private final FuTableComponent<T> fuTableComponent;

    public FuTableView(FuTableComponent<T> fuTableComponent) {
        super(fuTableComponent);
        this.fuTableComponent = fuTableComponent;
    }


    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        FuTableListener<T> fuTableListener;
        if (Objects.nonNull(this.fuTableComponent) && Objects.nonNull(fuTableListener = this.fuTableComponent.getFuTableListener())) {
            TableCellEditor cellEditor = fuTableListener.getCellEditor(this, row, column);
            if (Objects.nonNull(cellEditor)) {
                return cellEditor;
            }
        }
        return super.getCellEditor(row, column);
    }
}
