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
    private Boolean select;

    /**
     * 请求参数类型 默认文本
     */
    private String requestParamType;

    /**
     * key
     */
    private String key;

    /**
     * value
     */
    private String value;

    /**
     * 描述信息
     */
    private String description;

    public KeyValueTableBO() {
        this.select = true;
        this.requestParamType = RequestParamType.TEXT.getCode();
    }
}
