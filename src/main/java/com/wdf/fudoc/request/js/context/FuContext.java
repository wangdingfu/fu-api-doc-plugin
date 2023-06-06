package com.wdf.fudoc.request.js.context;

import cn.hutool.json.JSON;
import lombok.Getter;
import lombok.Setter;


/**
 * @author wangdingfu
 * @date 2023-05-31 10:19:03
 */
@Getter
@Setter
public class FuContext {


    /**
     * 当前项目环境(针对当前项目)
     */
    private final FuEnv projectEnv = new FuEnv();

    /**
     * 全局环境(针对所有项目)
     */
    private final FuEnv globalEnv = new FuEnv();

    /**
     * 响应结果(配置的前置请求返回的结果)
     */
    private JSON result;
}
