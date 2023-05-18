package com.wdf.fudoc.storage.handler;

/**
 * 持久化数据到json文件 数据格式为json格式
 *
 * @author wangdingfu
 * @date 2023-05-15 21:25:01
 */
public abstract class AbstractJsonDataStorage<T> extends AbstractFuStorageHandler<T> {


    @Override
    public T loadDataFromDisk() {
        return super.loadData();
    }

    @Override
    protected void writeDataToDisk(T data) {

    }
}
