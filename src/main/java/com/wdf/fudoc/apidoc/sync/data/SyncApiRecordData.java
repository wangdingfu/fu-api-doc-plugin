package com.wdf.fudoc.apidoc.sync.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 已经同步过的接口文档记录
 *
 * @author wangdingfu
 * @date 2023-01-01 17:52:27
 */
@Getter
@Setter
public class SyncApiRecordData {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 第三方系统项目名称
     */
    private String projectName;

    /**
     * 第三方系统接口分类全路径名称(多级分类通过'/'拼接)
     */
    private String categoryName;

    /**
     * 接口api
     */
    private String apiKey;

    /**
     * 同步时间
     */
    private String syncTime;
}
