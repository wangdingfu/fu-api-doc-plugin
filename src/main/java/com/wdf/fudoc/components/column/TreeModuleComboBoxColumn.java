package com.wdf.fudoc.components.column;

import com.wdf.fudoc.common.base.FuFunction;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.components.factory.TableCellEditorFactory;
import com.wdf.fudoc.util.LambdaUtils;
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
    private FuFunction<T, TreePathBO> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, TreePathBO> setFun;

    public TreeModuleComboBoxColumn(String name, FuFunction<T, TreePathBO> getFun, BiConsumer<T, TreePathBO> setFun) {
        super(name, TableCellEditorFactory.createModuleTreeEditor());
        this.getFun = getFun;
        this.setFun = setFun;
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
