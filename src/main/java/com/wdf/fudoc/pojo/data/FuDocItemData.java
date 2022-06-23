package com.wdf.fudoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @descption: 生成每一个接口文档所需的数据（渲染一个接口的标准对象）
 * @date 2022-05-09 23:38:40
 */
@Getter
@Setter
public class FuDocItemData {

    /**
     * 接口序号
     */
    private String docNo;

    /**
     * 接口标题
     */
    private String title;

    /**
     * 接口详情信息
     */
    private String detailInfo;

    /**
     * 接口请求地址
     */
    private List<String> urlList;

    /**
     * 接口请求类型
     */
    private String requestType;


    /**
     * 请求示例
     */
    private String requestExample;


    /**
     * 请求参数
     */
    private List<FuDocParamData> requestParams;


    /**
     * 响应示例
     */
    private String responseExample;


    /**
     * 响应参数
     */
    private List<FuDocParamData> responseParams;

    /**
     * 扩展信息
     */
    private Map<String, Object> extInfo;
}
