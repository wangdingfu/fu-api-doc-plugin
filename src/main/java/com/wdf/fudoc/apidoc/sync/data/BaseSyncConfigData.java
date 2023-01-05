package com.wdf.fudoc.apidoc.sync.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 同步到第三方接口文档系统抽象配置
 *
 * @author wangdingfu
 * @date 2022-12-31 22:21:30
 */
@Getter
@Setter
public class BaseSyncConfigData {

    /**
     * 三方接口文档系统地址
     */
    private String baseUrl;

    /**
     * 同步接口地址
     */
    private String syncApiUrl;

    /**
     * 第三方接口文档系统项目配置
     */
    private Map<String, ApiSystemProjectConfigData> projectConfigDataMap;


    /**
     * 已经同步过的接口文档记录
     */
    private List<SyncApiRecordData> syncRecordList;

}
