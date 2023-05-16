package com.wdf.fudoc.storage.enumtype;

import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-05-15 13:00:17
 */
@Getter
public enum FuStorageType {


    FU_REQUEST("fu-request","存放请求信息",FuStorageFileType.JSON,"api", FuHttpRequestData.class),
    FU_CONFIG_YAPI("fu-config-yapi-project","存放yapi配置信息",FuStorageFileType.JSON,"config", FuDocSyncProjectSetting.class),


    ;
    private final String code;

    private final String msg;

    private final FuStorageFileType storageFileType;

    private final String path;

    private final Class<?> clazz;

    FuStorageType(String code, String msg, FuStorageFileType storageFileType, String path, Class<?> clazz) {
        this.code = code;
        this.msg = msg;
        this.storageFileType = storageFileType;
        this.path = path;
        this.clazz = clazz;
    }
}
