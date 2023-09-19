package com.wdf.fudoc.common.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-09-19 22:32:27
 */
@Getter
public enum FuDocAction {

    INIT("init"),
    GEN_DOC("Gen Doc"),
    SYNC_API("Sync Api"),
    FU_REQUEST("Fu Request"),
    GEN_CURL("Gen Curl"),
    NAV_URL("Fu Api"),
    BEAN_TO_JSON("Bean to Json"),
    BEAN_COPY("beanCopy"),
    CLOSE("close"),



    ;

    private final String code;

    FuDocAction(String code) {
        this.code = code;
    }
}
