package com.wdf.fudoc.components.column;

import com.wdf.fudoc.components.factory.TableCellEditorFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:20:00
 */
@Getter
@Setter
public class DynamicColumn extends Column {

    /**
     * 字段名称
     */
    private String fieldName;

    public DynamicColumn(String name, String fieldName) {
        super(name, TableCellEditorFactory.createTextFieldEditor());
        this.fieldName = fieldName;
    }

    @Override
    public Class<?> getColumnClass() {
        return Object.class;
    }
}
