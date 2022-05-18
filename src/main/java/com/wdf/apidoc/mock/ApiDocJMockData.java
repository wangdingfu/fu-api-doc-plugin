package com.wdf.apidoc.mock;

import com.github.jsonzou.jmockdata.JMockData;

/**
 * @author wangdingfu
 * @Descption JMockData 框架mock数据
 * @Date 2022-05-18 21:37:14
 */
public class ApiDocJMockData extends AbstractApiDocMockData {


    /**
     * JMockData框架mock数据
     *
     * @param classType java class类型
     * @param name      字段名
     * @param <T>       泛型
     * @return mock的值
     */
    @Override
    protected <T> T mock(Class<T> classType, String name) {
        return JMockData.mock(classType);
    }
}
