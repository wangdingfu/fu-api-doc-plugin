package com.wdf.apidoc.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @descption: API接口文档参数解析后的数据对象(存放了指定参数的所有信息)
 * @author wangdingfu
 * @date 2022-04-05 20:04:47
 */
@Getter
@Setter
public class ApiDocObjectData extends AnnotationDataMap {

    /**
     * 全称
     */
    private String qualifiedName;

    /**
     * 带泛型全名称(一个对象的唯一性)
     */
    private String canonicalText;

    /**
     * 字段集合
     */
    private List<ApiDocObjectFieldData> fieldDataList;
}
