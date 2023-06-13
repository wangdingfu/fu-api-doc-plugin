package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-12 18:14:39
 */
@Getter
@Setter
public class OpenApiParameterItemDTO {

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数位置，可以是 path、query、header、cookie
     */
    private String in;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 参数示例
     */
    private String example;

    /**
     * 参数格式
     */
    private YApiJsonSchema schema;
}
