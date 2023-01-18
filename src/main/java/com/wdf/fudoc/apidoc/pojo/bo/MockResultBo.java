package com.wdf.fudoc.apidoc.pojo.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * mock数据结果
 *
 * @author wangdingfu
 * @date 2022-11-16 22:57:20
 */
@Getter
@Setter
public class MockResultBo {

    /**
     * 请求参数示例
     */
    private String requestExample;

    /**
     * 请求参数示例类型 (json|yaml)
     */
    private String requestExampleType;

    /**
     * 响应参数示例
     */
    private String responseExample;

    /**
     * 请求结果示例类型 (json|yaml)
     */
    private String responseExampleType;
}
