package com.wdf.fudoc.components.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:53:13
 */
public class DynamicTableBO {
    private final Map<String, Object> dataMap = new HashMap<>();


    public Object getValue(String name) {
        return dataMap.get(name);
    }

    public void setValue(String name, Object value) {
        dataMap.put(name, value);
    }
}
