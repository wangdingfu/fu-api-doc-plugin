package com.wdf.fudoc.common.constant;

/**
 * @author wangdingfu
 * @descption: 消息key常量类
 * @date 2022-05-30 23:46:23
 */
public interface MessageConstants {

    String MESSAGE_BUNDLE = "messages.MyBundle";


    String FU_DOC = "fudoc";


    String READ_DOC_ACTION = "fudoc.document.action";
    String FAQ_ACTION = "fudoc.faq.action";
    String STAR_ACTION = "fudoc.star.action";

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


    String FU_REQUEST_DOWNLOAD_FILE_FAIL = "fudoc.request.download.file.fail";
    String FU_REQUEST_SELECT_DIR_TITLE = "fudoc.request.response.select.dir.title";
    String FU_REQUEST_DOWNLOAD_NOT_FILE = "fudoc.request.download.not.file";


    /**
     * 通知无法获取到类
     */
    String NOTIFY_NOT_FUND_CLASS = "notify.not.fund.class";


    String SYNC_YAPI_SUCCESS = "fudoc.sync.yapi.success";
    String SYNC_YAPI_FAIL = "fudoc.sync.yapi.fail";


    String SYNC_YAPI_BASE_TITLE = "fudoc.sync.yapi.base.title";
    String SYNC_YAPI_MAIN_TITLE = "fudoc.sync.yapi.main.title";


    String SYNC_YAPI_TOKEN = "fodoc.sync.yapi.token";
    String SYNC_YAPI_TOKEN_TITLE = "fodoc.sync.yapi.token.title";


    String SYNC_YAPI_URL_TIP = "fodoc.sync.yapi.url.tip";

    String SYNC_YAPI_GET_PROJECT_FAIL = "fudoc.sync.yapi.project.fail";
    String SYNC_API_NOT_CONFIRM_CATEGORY = "fudoc.sync.no.category";

    String NOT_SYNC_API = "fudoc.sync.no";
    String SYNC_API_SUCCESS_ONE = "fudoc.api.sync.success.one";
    String SYNC_API_SUCCESS_ALL = "fudoc.api.sync.success.all";
    String SYNC_API_FAILED_ALL = "fudoc.api.sync.failed.all";
    String SYNC_API_SUCCESS_FAILED = "fudoc.api.sync.success.fail";

    String SYNC_API_TITLE = "fudoc.api.sync.category.title";
    String SYNC_API_PROJECT_LABEL = "fudoc.api.sync.category.project.label";
    String SYNC_API_CATEGORY_LABEL = "fudoc.api.sync.category.category.label";
    String SYNC_API_CREATE_PROJECT = "fudoc.api.sync.create.project";
    String SYNC_API_CREATE_PROJECT_TITLE = "fudoc.api.sync.create.project.title";
    String SYNC_API_CREATE_CATEGORY = "fudoc.api.sync.create.category";
    String SYNC_API_CREATE_CATEGORY_TITLE = "fudoc.api.sync.create.category.title";
    String SYNC_API_CREATE_CATEGORY_REPEAT = "fudoc.api.sync.create.category.repeat";


    String VALIDATOR_INPUT_REPEAT = "fudoc.input.validator.repeat";


}
