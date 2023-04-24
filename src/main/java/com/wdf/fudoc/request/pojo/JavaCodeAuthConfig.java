package com.wdf.fudoc.request.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:19:51
 */
@Getter
@Setter
public class JavaCodeAuthConfig extends BaseAuthConfig{

    /**
     * java代码
     */
    private String javaCode;
}
