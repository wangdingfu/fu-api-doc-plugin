package com.wdf.fudoc.components.column;

import com.wdf.fudoc.common.base.FuFunction;
import com.wdf.fudoc.util.LambdaUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.TableCellEditor;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-09-05 16:10:20
 */
@Getter
@Setter
public class BooleanColumn<T> extends Column {

    /**
     * get方法
     */
    private FuFunction<T, Boolean> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, Boolean> setFun;

    public BooleanColumn(String name, FuFunction<T, Boolean> getFun, BiConsumer<T, Boolean> setFun) {
        super(name, null);
        this.getFun = getFun;
        this.setFun = setFun;
    }

    public BooleanColumn(String name, FuFunction<T, Boolean> getFun, BiConsumer<T, Boolean> setFun, TableCellEditor editor) {
        super(name, editor);
        this.getFun = getFun;
        this.setFun = setFun;
    }

    @Override
    public Class<?> getColumnClass() {
        return Boolean.class;
    }

    @Override
    public String getFieldName() {
        return LambdaUtils.getPropertyName(this.getFun);
    }
}
