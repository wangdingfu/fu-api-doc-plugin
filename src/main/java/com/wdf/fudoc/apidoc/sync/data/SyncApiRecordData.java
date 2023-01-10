package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
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
     * 接口地址
     */
    private String apiUrl;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 接口分类
     */
    private ApiCategoryDTO category;

    /**
     * 同步时间
     */
    private String syncTime;
}
