package com.wdf.fudoc.view.components;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:24:45
 */
public class StringColumnInfo<T> extends EditableColumnInfo<T, String> {

    /**
     * get方法
     */
    private Function<T, String> getFun;
    /**
     * set方法
     */
    private BiConsumer<T, String> setFun;

    public StringColumnInfo(String name, Function<T, String> getFun, BiConsumer<T, String> setFun) {
        super(name);
        this.getFun = getFun;
        this.setFun = setFun;
    }

    public StringColumnInfo(Function<T, String> getFun, BiConsumer<T, String> setFun) {
        super();
        this.getFun = getFun;
        this.setFun = setFun;
    }


    @Override
    public @Nullable String valueOf(T t) {
        return getFun.apply(t);
    }


    @Override
    public void setValue(T t, String value) {
        setFun.accept(t, value);
    }
}
