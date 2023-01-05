package com.wdf.fudoc.start;

import lombok.Getter;
import lombok.Setter;

/**
 * 通知信息对象
 *
 * @author wangdingfu
 * @date 2022-12-30 23:14:23
 */
@Getter
@Setter
public class NotifyInfoVO {

    /**
     * 通知类型 1-插件更新通知 2-更新公告 3-通知
     */
    private Integer notifyType;


    /**
     * 展示类型 1-对话框展示 2-右下角通知提示
     */
    private Integer showType;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;


    /**
     * 提示框宽度-可选
     */
    private Integer width;


    /**
     * 提示框高度-可选
     */
    private Integer height;


}
