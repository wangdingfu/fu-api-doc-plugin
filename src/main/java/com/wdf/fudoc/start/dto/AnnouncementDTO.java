package com.wdf.fudoc.start.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 公告信息
 *
 * @author wangdingfu
 * @date 2023-01-12 20:54:12
 */
@Getter
@Setter
public class AnnouncementDTO {

    /**
     * 插件版本
     */
    private String pluginVersion;

    /**
     * 公告id
     */
    private String id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告窗口展示宽度
     */
    private Integer width;

    /**
     * 公告窗口展示高度
     */
    private Integer height;
}
