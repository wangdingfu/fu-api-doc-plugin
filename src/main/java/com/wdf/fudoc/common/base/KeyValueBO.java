package com.wdf.fudoc.common.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-01-09 18:38:19
 */
@Getter
@Setter
public class KeyValueBO {

    private String key;

    private String value;

    public KeyValueBO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
