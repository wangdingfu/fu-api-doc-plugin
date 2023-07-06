package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-05 17:04:59
 */
@Getter
@Setter
public class ApiFoxResult {

    private Boolean success;

    private String errorCode;

    private String errorMessage;
}
