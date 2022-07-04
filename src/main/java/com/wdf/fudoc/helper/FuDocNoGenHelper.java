package com.wdf.fudoc.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @Descption 接口文档序号生成器
 * @Date 2022-06-23 22:01:19
 */
public class FuDocNoGenHelper {

    private static final Map<String, Integer> PARAM_NO_MAP = new ConcurrentHashMap<>();


    public synchronized static Integer genNo(String methodId) {
        if (StringUtils.isBlank(methodId)) {
            return 0;
        }
        Integer value = PARAM_NO_MAP.get(methodId);
        if (Objects.isNull(value)) {
            value = 0;
        }
        value = value + 1;
        PARAM_NO_MAP.put(methodId, value);
        return value;
    }

}
