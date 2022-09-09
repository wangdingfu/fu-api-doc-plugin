package com.wdf.fudoc.apidoc.pojo.desc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 方法信息描述
 * @date 2022-05-08 22:22:20
 */
@Getter
@Setter
public class MethodInfoDesc extends BaseInfoDesc {

    /**
     * 方法唯一标识
     */
    private String methodId;

    /**
     * 请求参数集合
     */
    private List<ObjectInfoDesc> requestList;

    /**
     * 返回参数
     */
    private ObjectInfoDesc response;
}
