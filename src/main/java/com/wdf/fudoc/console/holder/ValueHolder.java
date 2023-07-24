package com.wdf.fudoc.console.holder;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-24 21:14:14
 */
public class ValueHolder<T> extends AbstractHolder<T> {

    private T data;

    public ValueHolder() {
    }

    @Override
    protected boolean isComputed() {
        return Objects.nonNull(this.data);
    }

    @Override
    protected T data() {
        return this.data;
    }


    public void success(T data) {
        lock.lock();
        try {
            this.data = data;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
