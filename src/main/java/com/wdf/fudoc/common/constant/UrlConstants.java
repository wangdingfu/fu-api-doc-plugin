package com.wdf.fudoc.common.constant;

/**
 * @author wangdingfu
 * @date 2022-08-21 00:54:08
 */
public interface UrlConstants {


    String DOCUMENT = "https://wangdingfu.github.io/#/zh-cn/?id=fudoc";

    String GITHUB = "https://github.com/wangdingfu/fu-api-doc-plugin";


    String ISSUE = GITHUB + "/issues";

    String GITEE = "https://gitee.com/wdfu/fudoc";

    String GITEE_ISSUE = GITEE + "/issues";


    String FU_DOC_URL = "http://150.158.164.160:9090";

    String FU_DOCUMENT_SYNC_URL = "https://wangdingfu.github.io/#/zh-cn/sync/quickstart";


    String API_FOX = "https://api.apifox.cn";
    String SHOW_DOC = "https://www.showdoc.cc";


    String SHOW_DOC_SYNC_API = "/api/item/updateByApi";

    String SHOW_DOC_DEFAULT = "/server" + SHOW_DOC_SYNC_API;

    String SHOW_DOC_PRIVATE_SYNC_API = "/server/index.php?s=" + SHOW_DOC_SYNC_API;

}
