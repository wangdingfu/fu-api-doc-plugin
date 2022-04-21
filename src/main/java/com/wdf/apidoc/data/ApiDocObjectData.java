package com.wdf.apidoc.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @descption: 参数字段属性数据对象
 * @author wangdingfu
 * @date 2022-04-05 22:10:26
 */
@Getter
@Setter
public class ApiDocObjectData extends AnnotationDataMap{

    /**
     * 字段类型
     */
    private String type;

    /**
     * 页面显示的类型
     */
    private String typeView;

    /**
     * 字段名称
     */
    private String name;


    /**
     * 字段注释
     */
    private String docText;

    /**
     * 必填属性
     */
    private boolean required;


    /**
     * 子属性字段集合(当前对象不为基本对象 且有自己属性字段时)
     */
    private List<ApiDocObjectData> childList;
}
