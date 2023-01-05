package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Yapi需要的Json结构
 *
 * @author wangdingfu
 * @date 2023-01-05 16:32:20
 */
@Getter
@Setter
public class YApiJsonSchema {

    /**
     * 属性类型
     */
    private String type;

    /**
     * 必填属性 当type为object或者array时 require为对象中必填的属性集合
     */
    private List<String> required;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 当type为array时 items有值
     */
    private YApiJsonSchema items;

    /**
     * 当type为object时 properties有值
     */
    private Map<String, YApiJsonSchema> properties;
}
