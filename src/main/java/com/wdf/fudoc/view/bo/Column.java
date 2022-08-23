package com.wdf.fudoc.view.bo;

import com.wdf.fudoc.factory.TableCellEditorFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.table.TableCellEditor;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-08-16 22:40:37
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Column<T> {
    /**
     * 列名
     */
    private String name;
    /**
     * get方法
     */
    private Function<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    /**
     * 列编辑器
     */
    private TableCellEditor editor;

    public Column(String name, Function<T, String> getFun, BiConsumer<T, String> setFun) {
        this.name = name;
        this.getFun = getFun;
        this.setFun = setFun;
        this.editor = TableCellEditorFactory.createTextFieldEditor();
    }
}
