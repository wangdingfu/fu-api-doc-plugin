package com.wdf.fudoc.components.bo;

import com.wdf.fudoc.components.factory.TableCellEditorFactory;
import lombok.Getter;
import lombok.Setter;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-09-06 11:08:57
 */
@Getter
@Setter
public class TreeModuleComboBoxColumn<T> extends Column {

    /**
     * get方法
     */
    private Function<T, TreePathBO> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, TreePathBO> setFun;

    public TreeModuleComboBoxColumn(String name, Function<T, TreePathBO> getFun, BiConsumer<T, TreePathBO> setFun) {
        super(name, TableCellEditorFactory.createModuleTreeEditor());
        this.getFun = getFun;
        this.setFun = setFun;
    }

    @Override
    public Class<?> getColumnClass() {
        return String.class;
    }
}
