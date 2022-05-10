package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @descption: 注解map公共对象
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


    /**
     * 获取注解
     *
     * @param annotationName 注解名称
     * @return 注解数据
     */
    public Optional<AnnotationData> getAnnotation(String annotationName) {
        if (Objects.nonNull(annotationDataMap) && StringUtils.isNotBlank(annotationName)) {
            return Optional.ofNullable(this.annotationDataMap.get(annotationName));
        }
        return Optional.empty();
    }


    /**
     * 是否存在指定注解
     *
     * @param annotationNames 注解名称集合
     * @return true: 存在指定注解 false: 不存在
     */
    public boolean exists(String... annotationNames) {
        if (Objects.nonNull(annotationDataMap) && Objects.nonNull(annotationNames) && annotationNames.length > 0) {
            for (String annotationName : annotationNames) {
                if (annotationDataMap.containsKey(annotationName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
