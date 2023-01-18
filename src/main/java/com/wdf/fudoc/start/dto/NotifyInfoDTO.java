package com.wdf.fudoc.start.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 通知消息
 *
 * @author wangdingfu
 * @date 2023-01-12 20:59:26
 */
@Getter
@Setter
public class NotifyInfoDTO {

    /**
     * 提示标题
     */
    private String title;

    /**
     * 提示内容
     */
    private String content;

    /**
     * 行为集合
     */
    private List<NotificationActionDTO> actionList;
}
