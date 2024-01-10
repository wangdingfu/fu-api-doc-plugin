package com.wdf.fudoc.components.column;

import com.wdf.fudoc.common.base.FuFunction;
import com.wdf.fudoc.components.FuTableComboBoxRenderer;
import com.wdf.fudoc.components.JLabelListRendererComponent;
import com.wdf.fudoc.components.factory.TableCellEditorFactory;
import com.wdf.fudoc.util.LambdaUtils;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author wangdingfu
 * @date 2022-09-06 11:08:57
 */
@Getter
@Setter
public class ComboBoxColumn<T> extends Column {

    /**
     * get方法
     */
    private FuFunction<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    private final Icon icon;

    public ComboBoxColumn(String name, Icon icon, FuFunction<T, String> getFun, BiConsumer<T, String> setFun) {
        super(name, TableCellEditorFactory.createTextFieldEditor());
        this.icon = icon;
        this.getFun = getFun;
        this.setFun = setFun;
    }

    public ComboBoxColumn(String name, Icon icon, FuFunction<T, String> getFun, BiConsumer<T, String> setFun, Set<String> items) {
        super(name, TableCellEditorFactory.createComboBoxEditor(false, icon, items));
        this.icon = icon;
        this.getFun = getFun;
        this.setFun = setFun;
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        //AllIcons.Nodes.Module
        if (Objects.nonNull(this.icon)) {
            return new FuTableComboBoxRenderer(icon);
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass() {
        return Enum.class;
    }

    @Override
    public String getFieldName() {
        return LambdaUtils.getPropertyName(this.getFun);
    }
}
