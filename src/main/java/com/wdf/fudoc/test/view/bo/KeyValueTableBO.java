package com.wdf.fudoc.test.view.bo;

import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-09-01 11:43:33
 */
@Getter
@Setter
@AllArgsConstructor
public class KeyValueTableBO {

    /**
     * 默认选中
     */
    protected Boolean select;

    /**
     * 请求参数类型 默认文本
     */
    protected String requestParamType;

    /**
     * key
     */
    protected String key;

    /**
     * value
     */
    protected String value;

    /**
     * 描述信息
     */
    protected String description;

    public KeyValueTableBO() {
        this.select = true;
        this.requestParamType = RequestParamType.TEXT.getCode();
    }


    public KeyValueTableBO(Boolean select, String key, String value) {
        this.select = select;
        this.requestParamType = RequestParamType.TEXT.getCode();
        this.key = key;
        this.value = value;
    }

    public KeyValueTableBO(Boolean select, String key, String value, String description) {
        this.select = select;
        this.key = key;
        this.value = value;
        this.description = description;
    }
}
