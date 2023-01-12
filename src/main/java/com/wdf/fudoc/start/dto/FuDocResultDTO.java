package com.wdf.fudoc.start.dto;

import com.wdf.fudoc.components.bo.FuMsgBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 跟服务端交互的响应数据
 *
 * @author wangdingfu
 * @date 2023-01-12 20:56:35
 */
@Getter
@Setter
public class FuDocResultDTO {

    /**
     * 公告消息
     */
    private AnnouncementDTO announcement;

    /**
     * 通知消息-右下角弹框
     */
    private NotifyInfoDTO notifyInfo;

    /**
     * 消息集合
     */
    private List<FuMsgBO> msgList;
}
