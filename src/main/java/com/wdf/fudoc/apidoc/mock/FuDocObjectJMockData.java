package com.wdf.fudoc.apidoc.mock;

import com.github.jsonzou.jmockdata.JMockData;

/**
 * @author wangdingfu
 * @descption: JMockData 实现mock数据
 * @date 2022-05-22 21:49:10
 */
public class FuDocObjectJMockData implements FuDocObjectMock {

    /**
     * JMockData框架mock数据
     *
     * @param classType java class类型
     * @param name      字段名
     * @param <T>       泛型
     * @return mock的值
     */
    @Override
    public  <T> T mock(Class<T> classType, String name) {
        //TODO 优先从相关配置中mock常用数据
        return JMockData.mock(classType);
    }
}
