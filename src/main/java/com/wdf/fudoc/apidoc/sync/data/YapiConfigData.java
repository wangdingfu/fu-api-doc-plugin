package com.wdf.fudoc.apidoc.sync.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Yapi配置
 *
 * @author wangdingfu
 * @date 2023-01-05 11:52:04
 */
@Getter
@Setter
public class YapiConfigData extends BaseSyncConfigData {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;


    /**
     * 项目配置
     */
    private List<YapiProjectConfigData> projectConfigList;

}
