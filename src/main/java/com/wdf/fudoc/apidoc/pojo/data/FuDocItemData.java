package com.wdf.fudoc.apidoc.pojo.data;

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
public class FuDocItemData extends CommonItemData {

    /**
     * 接口序号
     */
    private String docNo;

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
    private Map<String, Object> fudoc;
}
