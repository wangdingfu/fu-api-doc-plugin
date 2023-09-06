package com.wdf.fudoc.common.constant;

/**
 * @author wangdingfu
 * @date 2022-08-21 00:54:08
 */
public interface UrlConstants {


    String DOCUMENT = "http://www.fudoc.cn";

    String GITHUB = "https://github.com/wangdingfu/fu-api-doc-plugin";


    String ISSUE = GITHUB + "/issues";

    String GITEE = "https://gitee.com/wdfu/fudoc";

    String GITEE_ISSUE = GITEE + "/issues";


    String FU_DOC_URL = "http://150.158.164.160:9090";

    String FU_DOCUMENT_SYNC_URL = "http://www.fudoc.cn/pages/0a1917/";
    String FU_DOCUMENT_SHOW_DOC_URL = "http://www.fudoc.cn/pages/9d2f46/";
    String FU_DOCUMENT_YAPI_URL = "http://www.fudoc.cn/pages/52ab08/";


    String API_FOX = "https://api.apifox.cn";
    String SHOW_DOC = "https://www.showdoc.cc";


    String SHOW_DOC_SYNC_API = "/api/item/updateByApi";

    String SHOW_DOC_DEFAULT = "/server" + SHOW_DOC_SYNC_API;

    String SHOW_DOC_PRIVATE_SYNC_API = "/server/index.php?s=" + SHOW_DOC_SYNC_API;

}
