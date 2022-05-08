package com.wdf.apidoc.pojo.desc;

import com.wdf.apidoc.pojo.data.AnnotationDataMap;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
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
public class MethodInfoDesc extends AnnotationDataMap {

    /**
     * 方法上的注释对象
     */
    private ApiDocCommentData commentData;

    /**
     * 请求参数集合
     */
    private List<ObjectInfoDesc> requestList;

    /**
     * 返回参数
     */
    private ObjectInfoDesc response;
}
