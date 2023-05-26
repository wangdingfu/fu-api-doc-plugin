package com.wdf.fudoc.storage.enumtype;

import lombok.Getter;

/**
 * 存储文件类型
 * @author wangdingfu
 * @date 2023-05-15 13:01:26
 */
@Getter
public enum FuStorageFileType {
    /**
     * json格式文件
     */
    JSON(".json","json格式文件"),
    /**
     * fuApi格式文件
     */
    FU_API(".fuApi","fuApi格式文件");


    private final String suffix;

    private final String msg;

    FuStorageFileType(String suffix, String msg) {
        this.suffix = suffix;
        this.msg = msg;
    }
}
