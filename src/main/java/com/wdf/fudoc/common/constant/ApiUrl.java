package com.wdf.fudoc.common.constant;

/**
 * @author wangdingfu
 * @date 2023-01-12 22:03:49
 */
public interface ApiUrl {
    /**
     * ping
     */
    String PLUGIN_PING_API_URL = "/fu_doc_plugin/ping";
    /**
     * 获取插件信息
     */
    String PLUGIN_INFO_API_URL = "/fu_doc_plugin/info";
    /**
     * 已读公告
     */
    String SEND_IS_READ_API_URL = "/fu_doc_plugin/confirm";


    String ACCESS_TOKEN = "/fu_doc_plugin/access-token";
}
