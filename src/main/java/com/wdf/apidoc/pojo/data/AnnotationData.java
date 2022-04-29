package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author wangdingfu
 * @descption: 注解属性数据对象
 * @date 2022-04-05 21:16:23
 */
@Getter
@Setter
public class AnnotationData {

    /**
     * 注解全路径
     */
    private String qualiName;

    /**
     * 注解value值
     */
    private String value;


    /**
     * 注解属性map
     * key:属性名  value:属性值
     */
    private Map<String, String> attrMap;


}
