package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * api同步状态
 *
 * @author wangdingfu
 * @date 2023-01-05 13:52:50
 */
@Getter
public enum ApiSyncStatus {

    TO_SYNC("1", "待同步"),
    SYNCING("2", "同步中"),
    SUCCESS("3", "同步成功"),
    FAIL("4", "同步失败"),
    ;

    private final String code;

    private final String message;

    ApiSyncStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
