package com.wdf.fudoc.start.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求是否有更新或者公告提示信息等
 *
 * @author wangdingfu
 * @date 2023-01-01 23:46:07
 */
@Getter
@Setter
public class UpdateInfoDTO {

    /**
     * 唯一标识
     */
    private String uniqId;

    /**
     * 插件版本
     */
    private String version;

}
