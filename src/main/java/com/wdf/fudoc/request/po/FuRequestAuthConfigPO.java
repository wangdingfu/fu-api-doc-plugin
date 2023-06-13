package com.wdf.fudoc.request.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-08 00:01:42
 */
@Getter
@Setter
public class FuRequestAuthConfigPO {

    /**
     * 鉴权配置名称
     */
    private String name;

    /**
     * 作用域
     */
    private Integer scope;
    
}
