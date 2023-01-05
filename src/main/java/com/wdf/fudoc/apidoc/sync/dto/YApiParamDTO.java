package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * YApi请求参数对象
 *
 * @author wangdingfu
 * @date 2023-01-05 15:22:11
 */
@Getter
@Setter
public class YApiParamDTO {

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数实例
     */
    private String example;

    /**
     * 参数描述
     */
    private String desc;

    /**
     * 枚举: 1,0
     */
    private String required;
}
