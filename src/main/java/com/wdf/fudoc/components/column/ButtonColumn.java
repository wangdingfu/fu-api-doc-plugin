package com.wdf.fudoc.components.column;

import com.wdf.fudoc.common.base.FuFunction;
import com.wdf.fudoc.components.ButtonTableCellEditor;
import com.wdf.fudoc.util.LambdaUtils;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.table.TableCellRenderer;
import java.util.function.BiConsumer;


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
    private FuFunction<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    private ButtonTableCellEditor buttonTableCellEditor;

    public ButtonColumn(String name, FuFunction<T, String> getFun, BiConsumer<T, String> setFun, ButtonTableCellEditor cellEditor) {
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

    @Override
    public String getFieldName() {
        return LambdaUtils.getPropertyName(this.getFun);
    }
}
