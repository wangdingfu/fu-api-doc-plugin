package com.wdf.fudoc.pojo.dto;

import lombok.Getter;
import lombok.Setter;

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

}
