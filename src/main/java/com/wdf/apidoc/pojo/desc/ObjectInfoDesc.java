package com.wdf.apidoc.pojo.desc;

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
     * 是否过滤(即不展示当前对象)
     */
    private boolean isFilterObject;

    /**
     * 子属性字段集合(当前对象不为基本对象 且有自己属性字段时)
     */
    private List<ObjectInfoDesc> childList;

}
