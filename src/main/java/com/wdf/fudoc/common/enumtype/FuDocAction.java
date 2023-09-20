package com.wdf.fudoc.common.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-09-19 22:32:27
 */
@Getter
public enum FuDocAction {

    INIT("init", false),
    GEN_DOC("Gen Doc", true),
    SYNC_API("Sync Api", true),
    FU_REQUEST("Fu Request", true),
    GEN_CURL("Gen Curl", true),
    NAV_URL("Fu Api", true),
    BEAN_TO_JSON("Bean to Json", true),
    BEAN_COPY("beanCopy", true),
    CLOSE("close", false),


    ;

    private final String code;
    private final boolean valid;

    FuDocAction(String code, boolean valid) {
        this.code = code;
        this.valid = valid;
    }

    public static boolean isValid(String action) {
        for (FuDocAction value : FuDocAction.values()) {
            if (value.getCode().equals(action)) {
                return value.valid;
            }
        }
        return false;
    }
}
