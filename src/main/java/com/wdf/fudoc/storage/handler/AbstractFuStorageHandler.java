package com.wdf.fudoc.storage.handler;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-15 19:54:13
 */
public abstract class AbstractFuStorageHandler<T> implements FuStorageHandler<T> {

    /**
     * 数据内容
     */
    protected T data;

    /**
     * 从磁盘里加载文件并将文件内容转换成需要的数据格式返回
     *
     * @return 内存操作的数据对象
     */
    protected abstract T loadDataFromDisk();

    /**
     * 将指定的数据持久化到磁盘上
     *
     * @param data 内存操作的数据对象
     */
    protected abstract void writeDataToDisk(T data);

    @Override
    public T loadData() {
        if (Objects.isNull(this.data)) {
            return loadDataFromDisk();
        }
        return this.data;
    }

    @Override
    public void saveData(T data) {
        this.data = data;
        writeDataToDisk(data);
    }
}
