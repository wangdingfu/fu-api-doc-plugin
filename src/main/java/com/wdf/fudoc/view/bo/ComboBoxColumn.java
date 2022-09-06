package com.wdf.fudoc.view.bo;

import com.wdf.fudoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.factory.TableCellEditorFactory;
import lombok.Getter;
import lombok.Setter;

import javax.swing.table.TableCellEditor;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-09-06 11:08:57
 */
@Getter
@Setter
public class ComboBoxColumn<T> extends Column{

    /**
     * get方法
     */
    private Function<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    public ComboBoxColumn(String name, Function<T, String> getFun, BiConsumer<T, String> setFun) {
        super(name, TableCellEditorFactory.createTextFieldEditor());
        this.getFun = getFun;
        this.setFun = setFun;
    }

    public ComboBoxColumn(String name, Function<T, String> getFun, BiConsumer<T, String> setFun, Set<String> items) {
        super(name, TableCellEditorFactory.createComboBoxEditor(false, items));
        this.getFun = getFun;
        this.setFun = setFun;
    }


    @Override
    public Class<?> getColumnClass() {
        return Enum.class;
    }
}
