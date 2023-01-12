package com.wdf.fudoc.start.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-08-21 01:12:54
 */
@Getter
@Setter
public class NotificationActionDTO {

    /**
     * 行为类型
     */
    private String actionType;

    /**
     * 行为名称
     */
    private String actionName;

    /**
     * 行为内容
     */
    private String actionContent;

    public NotificationActionDTO() {
    }

    public NotificationActionDTO(String actionName, String actionContent) {
        this.actionName = actionName;
        this.actionContent = actionContent;
    }
}
