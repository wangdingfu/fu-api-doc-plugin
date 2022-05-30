package com.wdf.apidoc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wangdingfu
 * @descption: json工具类
 * @date 2022-05-31 00:10:21
 */
public class FastJsonUtils {

    /**
     * 格式化json字符串
     *
     * @param object 需要格式化的数据
     * @return 格式化后的json字符串
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }
}
