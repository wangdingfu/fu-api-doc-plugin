package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口同步结果对象
 *
 * @author wangdingfu
 * @date 2023-01-09 13:58:53
 */
@Getter
@Setter
public class SyncApiResultDTO {

    /**
     * 文档id
     */
    private String apiId;
    /**
     * 接口地址
     */
    private String apiUrl;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 接口分类名称
     */
    private String categoryId;

    /**
     * 接口分类名称
     */
    private String categoryName;

    /**
     * 同步状态
     */
    private String syncStatus;

    /**
     * 失败信息
     */
    private String errorMsg;

    private String btnText = "重新同步";
}
