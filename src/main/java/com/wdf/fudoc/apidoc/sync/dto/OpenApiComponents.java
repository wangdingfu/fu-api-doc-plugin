package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-06-12 18:28:41
 */
@Getter
@Setter
public class OpenApiComponents {

    /**
     * 对象的schema
     */
    private Map<String, OpenApiSchema> schemas;
}
