package com.wdf.fudoc.mock;

/**
 * @author wangdingfu
 * @descption: ApiDoc mock数据
 * @date 2022-05-22 21:47:46
 */
public interface FuDocObjectMock {

    /**
     * mock指定类型的数据
     *
     * @param classType java class类型
     * @param name      字段名
     * @param <T>       泛型
     * @return mock的数据
     */
    <T> T mock(Class<T> classType, String name);
}
