package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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
     * 根据多个注解名称遍历查询 查询到注解则消费该注解的数据 并退出
     * @param annotationNames 注解名称集合
     * @param annotationDataConsumer 注解数据消费者
     */
    public void consumerAnnotation(String[] annotationNames, Consumer<AnnotationData> annotationDataConsumer) {
        if (Objects.isNull(annotationDataConsumer) || Objects.isNull(annotationNames) || annotationNames.length > 0) {
            for (String annotationName : annotationNames) {
                Optional<AnnotationData> annotation = getAnnotation(annotationName);
                if (annotation.isPresent()) {
                    annotationDataConsumer.accept(annotation.get());
                    break;
                }
            }
        }
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
