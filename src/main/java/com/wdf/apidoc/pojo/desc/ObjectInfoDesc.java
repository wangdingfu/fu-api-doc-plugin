package com.wdf.apidoc.pojo.desc;

import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.pojo.data.AnnotationDataMap;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 对象信息描述
 * @date 2022-05-08 22:22:39
 */
@Getter
@Setter
public class ObjectInfoDesc extends AnnotationDataMap {

    /**
     * 对象类型枚举
     */
    private ApiDocObjectType apiDocObjectType;

    /**
     * 泛型类型
     */
    private ApiDocObjectType genericsType;

    /**
     * 是否为真实属性字段（存在泛型或则解析需要会虚拟出该对象）
     */
    private boolean isAttr;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 显示在页面的字段类型
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
     * 针对该类型mock的值
     */
    private Object value;

    /**
     * 子属性字段集合(当前对象不为基本对象 且有自己属性字段时)
     */
    private List<ObjectInfoDesc> childList;


}
