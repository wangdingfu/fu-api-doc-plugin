package com.wdf.fudoc.constant;

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

    String NOTIFY_TO_JSON_OK = "notify.toJson.ok";


    /**
     * 通知生成接口文档失败
     */
    String NOTIFY_GEN_FAIL = "notify.gen.fail";
    String NOTIFY_TO_JSON_FAIL = "notify.toJson.fail";
    /**
     * 没有内容可以生成
     */
    String NOTIFY_GEN_NO_CONTENT = "notify.gen.no.content";


    /**
     * 通知无法获取到类
     */
    String NOTIFY_NOT_FUND_CLASS = "notify.not.fund.class";

}
