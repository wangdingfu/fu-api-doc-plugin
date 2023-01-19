package com.wdf.fudoc.components.bo;

import com.wdf.fudoc.components.ButtonTableCellEditor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.table.TableCellRenderer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @author wangdingfu
 * @date 2023-01-19 15:41:49
 */
@Getter
@Setter
public class ButtonColumn<T> extends Column {

    /**
     * get方法
     */
    private Function<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    private ButtonTableCellEditor buttonTableCellEditor;

    public ButtonColumn(String name, Function<T, String> getFun, BiConsumer<T, String> setFun, ButtonTableCellEditor cellEditor) {
        super(name, cellEditor);
        this.buttonTableCellEditor = cellEditor;
        this.getFun = getFun;
        this.setFun = setFun;
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        return buttonTableCellEditor.getCellRenderer();
    }

    @Override
    public Class<?> getColumnClass() {
        return String.class;
    }
}
