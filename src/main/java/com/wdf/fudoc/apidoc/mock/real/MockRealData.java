package com.wdf.fudoc.apidoc.mock.real;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * mock真实请求过的数据(实际上是从【Fu Request】请求过的示例数据中获取)
 *
 * @author wangdingfu
 * @date 2023-01-16 22:20:48
 */
public interface MockRealData {


    /**
     * 获取字符串格式的真实示例数据
     *
     * @param fieldName 字段名
     * @return 真实请求过的示例数据
     */
    default String dataStringData(String fieldName) {
        Object data = getData(fieldName);
        return Objects.nonNull(data) ? data.toString() : StringUtils.EMPTY;
    }

    /**
     * 获取指定字段名的真实示例数据
     *
     * @param fieldName 字段名 如果字段名为空 并且数据为数组时 随机获取数组中的一条数据
     * @return 真实请求过的数据
     */
    Object getData(String fieldName);

}
