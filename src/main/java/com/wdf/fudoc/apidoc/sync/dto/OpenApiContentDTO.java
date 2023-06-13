package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-13 10:30:35
 */
@Getter
@Setter
public class OpenApiContentDTO {

    private YApiJsonSchema schema;

    private String example;
}
