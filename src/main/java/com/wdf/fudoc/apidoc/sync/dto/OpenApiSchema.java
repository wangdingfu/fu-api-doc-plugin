package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-12 18:29:03
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OpenApiSchema extends YApiJsonSchema {

    @JsonProperty("$ref")
    private String ref;
}
