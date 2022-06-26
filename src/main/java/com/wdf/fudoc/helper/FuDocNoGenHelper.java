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


    public static Integer genNo(String methodId) {
        if (StringUtils.isNotBlank(methodId)) {
            return 0;
        }
        if (PARAM_NO_MAP.containsKey(methodId)) {
            Integer value = PARAM_NO_MAP.get(methodId);
            if (Objects.isNull(value)) {
                value = 0;
            }
            PARAM_NO_MAP.put(methodId, value + 1);
        }
        return PARAM_NO_MAP.get(methodId);
    }

}
