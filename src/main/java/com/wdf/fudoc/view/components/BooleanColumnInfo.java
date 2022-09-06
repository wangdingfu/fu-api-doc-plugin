package com.wdf.fudoc.view.components;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:24:45
 */
public class BooleanColumnInfo<T> extends EditableColumnInfo<T, Boolean> {

    /**
     * get方法
     */
    private final Function<T, Boolean> getFun;
    /**
     * set方法
     */
    private final BiConsumer<T, Boolean> setFun;

    public BooleanColumnInfo(String name, Function<T, Boolean> getFun, BiConsumer<T, Boolean> setFun) {
        super(name);
        this.getFun = getFun;
        this.setFun = setFun;
    }

    public BooleanColumnInfo(Function<T, Boolean> getFun, BiConsumer<T, Boolean> setFun) {
        super();
        this.getFun = getFun;
        this.setFun = setFun;
    }

    @Override
    public Class<?> getColumnClass() {
        return Boolean.class;
    }

    @Override
    public @Nullable Boolean valueOf(T t) {
        return getFun.apply(t);
    }


    @Override
    public void setValue(T t, Boolean value) {
        setFun.accept(t, value);
    }
}
