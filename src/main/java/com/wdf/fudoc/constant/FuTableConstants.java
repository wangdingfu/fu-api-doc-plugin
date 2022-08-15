package com.wdf.fudoc.constant;

import cn.hutool.core.lang.Dict;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fu Table 常量类
 *
 * @author wangdingfu
 * @date 2022-08-15 15:10:44
 */
public class FuTableConstants {


    public static final String GENERAL_CUSTOM_DATA = "general.custom.data";
    public static final String GENERAL_FILTER_FIELD = "general.filter.field";


    public static final Map<String, Dict> TABLE_TITLE_FIELD_MAP = new ConcurrentHashMap<>();

    static {
        //自定义数据table
        TABLE_TITLE_FIELD_MAP.put(GENERAL_CUSTOM_DATA, Dict.of("别名", "alias", "类型", "type", "值", "value"));
        //过滤属性table
        TABLE_TITLE_FIELD_MAP.put(GENERAL_FILTER_FIELD, Dict.of("类路径", "className", "字段名(多个用\",\"拼接. 为空则过滤该类所有属性)", "fieldNames"));
    }


    public static Dict get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return TABLE_TITLE_FIELD_MAP.get(key);
    }

}
