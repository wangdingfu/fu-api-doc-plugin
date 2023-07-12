package com.wdf.fudoc.components.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-01 21:12:12
 */
@Getter
@Setter
public class HeaderKeyValueBO extends KeyValueTableBO {

    public HeaderKeyValueBO(Boolean select, String requestParamType, String key, String value, String description) {
        super(select, requestParamType, key, value, description, true);
    }

    public HeaderKeyValueBO() {
    }

    public HeaderKeyValueBO(Boolean select, String key, String value) {
        super(select, key, value);
    }

    public HeaderKeyValueBO(Boolean select, String key, String value, String description) {
        super(select, key, value, description);
    }

    /**
     * 级别 接口请求头 全局请求头
     */
    private String level = "接口请求头";

}
