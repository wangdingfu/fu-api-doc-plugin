package com.wdf.fudoc.request.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-26 14:07:47
 */
@Getter
@Setter
public class FuCookiePO {

    private String name;

    private String value;

    private String domain;

    private String path;

    private String expires;

    private boolean httpOnly;

    private boolean secure;

}
