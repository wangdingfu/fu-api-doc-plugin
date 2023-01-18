package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * YApi请求Header
 *
 * @author wangdingfu
 * @date 2023-01-05 15:22:37
 */
@Getter
@Setter
public class YApiHeaderDTO {

    /**
     * 请求头名称
     */
    private String name;

    /**
     * 请求头value
     */
    private String value;

    /**
     * 请求头示例
     */
    private String example;

    /**
     * 请求头描述
     */
    private String desc;


}
