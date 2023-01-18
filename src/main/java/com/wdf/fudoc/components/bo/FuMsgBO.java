package com.wdf.fudoc.components.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-11-30 14:22:12
 */
@Getter
@Setter
public class FuMsgBO {

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息展示的权重
     */
    private Double weight;

    /**
     * 消息明细
     * 一条完整的消息有多段子消息构成
     */
    private List<FuMsgItemBO> itemList;
}
