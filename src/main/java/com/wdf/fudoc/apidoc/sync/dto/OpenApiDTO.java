package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * openApi文档格式对象
 *
 * @author wangdingfu
 * @date 2023-06-12 18:09:18
 */
@Getter
@Setter
public class OpenApiDTO {

    /**
     * 接口集合
     * key:接口地址 value: (key:请求类型(GET/POST) value:接口对象)
     */
    private Map<String, Map<String, OpenApiItemDTO>> paths;

    /**
     * 数据模型
     */
    private OpenApiComponents components;

}
