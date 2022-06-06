package com.wdf.apidoc.constant;

/**
 * @author wangdingfu
 * @descption: 消息key常量类
 * @date 2022-05-30 23:46:23
 */
public interface MessageConstants {

    String MESSAGE_BUNDLE = "messages.MyBundle";

    /**
     * 通知拷贝消息至剪贴板成功的key
     */
    String NOTIFY_COPY_OK = "notify.copy.ok";


    /**
     * 通知生成接口文档失败
     */
    String NOTIFY_GEN_FAIL = "notify.gen.fail";


    /**
     * 通知无法获取到类
     */
    String NOTIFY_NOT_FUND_CLASS = "notify.not.fund.class";
}
