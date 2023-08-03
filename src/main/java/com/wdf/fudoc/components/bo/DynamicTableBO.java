package com.wdf.fudoc.components.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:53:13
 */
@Getter
@Setter
public class DynamicTableBO {
    /**
     * 是否选中
     */
    private Boolean select = true;

    protected Map<String, Object> dataMap = new HashMap<>();


    public Object getValue(String name) {
        return dataMap.get(name);
    }

    public void setValue(String name, Object value) {
        dataMap.put(name, value);
    }


    public boolean isSelect() {
        return Objects.nonNull(this.select) && this.select;
    }
}
