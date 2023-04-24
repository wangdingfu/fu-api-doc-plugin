package com.wdf.fudoc.request.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:18:31
 */
@Getter
@Setter
public abstract class BaseAuthConfig {

    /**
     * 失效时间
     */
    private Long expireTime;


}
