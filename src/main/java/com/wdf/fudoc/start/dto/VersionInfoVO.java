package com.wdf.fudoc.start.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-08-18 11:22:07
 */
@Getter
@Setter
public class VersionInfoVO {


    /**
     * 唯一ID
     */
    private String uniqId;

    /**
     * 0:不通知
     * 1:通知
     */
    private int code;

    /**
     * 间隔时间
     */
    private int time;

    /**
     * 通知消息
     */
    private String message;


    /**
     * 提示标题
     */
    private String tipTitle;

    /**
     * 提示内容
     */
    private String tipContent;

    /**
     * 行为集合
     */
    private List<NotificationActionDTO> actionList;


}
