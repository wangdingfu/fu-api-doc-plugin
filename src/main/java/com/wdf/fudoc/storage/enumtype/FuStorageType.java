package com.wdf.fudoc.storage.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-05-15 13:00:17
 */
@Getter
public enum FuStorageType {


    FU_REQUEST("fu-request","存放请求信息",FuStorageFileType.JSON,"api"),
    FU_CONFIG_YAPI("fu-config-yapi-project","存放yapi配置信息",FuStorageFileType.JSON,"config"),


    ;
    private final String code;

    private final String msg;

    private final FuStorageFileType storageFileType;

    private final String path;

    FuStorageType(String code, String msg, FuStorageFileType storageFileType, String path) {
        this.code = code;
        this.msg = msg;
        this.storageFileType = storageFileType;
        this.path = path;
    }
}
