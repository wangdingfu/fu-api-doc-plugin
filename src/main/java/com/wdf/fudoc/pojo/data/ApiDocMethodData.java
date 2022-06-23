package com.wdf.fudoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @descption: API接口方法级别属性数据
 * @author wangdingfu
 * @date 2022-04-05 21:40:45
 */
@Getter
@Setter
public class ApiDocMethodData extends AnnotationDataMap{

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求参数
     */
    private List<ApiDocObjectData> requestList;


    /**
     * 响应参数
     */
    private ApiDocObjectData response;

}
