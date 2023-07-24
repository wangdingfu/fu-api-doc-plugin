package com.wdf.fudoc.console.holder;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangdingfu
 * @date 2023-07-24 21:08:18
 */
public abstract class AbstractHolder<T> implements Holder<T> {

    protected abstract boolean isComputed();

    protected abstract T data();

    protected final ReentrantLock lock = new ReentrantLock();

    protected final Condition condition = lock.newCondition();


    @Override
    public T value() {
        if (!isComputed()) {
            lock.lock();
            try {
                while (!isComputed()) {
                    condition.await();
                }
            } catch (Exception e) {
                return null;
            } finally {
                lock.unlock();
            }
        }
        return data();
    }
}
