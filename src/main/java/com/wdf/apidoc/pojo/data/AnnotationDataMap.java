package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @descption: 注解map公共对象
 * @author wangdingfu
 * @date 2022-04-05 22:08:40
 */
@Getter
@Setter
public class AnnotationDataMap {

    /**
     * 注解属性数据map
     * key: 注解key
     * value: 注解解析后的属性数据对象
     */
    private Map<String, AnnotationData> annotationDataMap;
}
