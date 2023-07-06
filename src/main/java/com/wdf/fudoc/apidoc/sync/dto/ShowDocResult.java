package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-05 20:18:03
 */
@Getter
@Setter
public class ShowDocResult {
    @JsonProperty("error_code")
    private String code;

    private Object data;

    @JsonProperty("error_message")
    private String message;

}
