package com.wdf.fudoc.request.constants.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2022-09-23 18:23:48
 */
@Getter
public enum RequestStatus {

    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    TIMEOUT("TIMEOUT"),
    ;


    private final String name;

    RequestStatus(String name) {
        this.name = name;
    }
}
