package com.wdf.fudoc.request.js.context;

import cn.hutool.json.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-05-31 10:19:03
 */
@Getter
@Setter
public class FuContext {


    /**
     * 环境变量
     */
    private final Map<String, Object> env = new HashMap<>();

    /**
     * 响应结果
     */
    private JSON result;
}
