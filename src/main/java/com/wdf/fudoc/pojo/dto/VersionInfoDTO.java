package com.wdf.fudoc.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-08-18 11:25:47
 */
@Getter
@Setter
public class VersionInfoDTO {

    /**
     * 唯一id
     */
    private String uniqId;

    /**
     * 插件版本
     */
    private String pluginVersion;

}
