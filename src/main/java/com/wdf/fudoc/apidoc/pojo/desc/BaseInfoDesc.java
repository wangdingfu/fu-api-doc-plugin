package com.wdf.fudoc.apidoc.pojo.desc;

import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author wangdingfu
 * @Description 描述对象抽象类
 * @date 2022-06-18 22:01:58
 */
@Getter
@Setter
public class BaseInfoDesc {

    /**
     * 注解属性数据map
     * key: 注解key
     * value: 注解解析后的属性数据对象
     */
    private Map<String, AnnotationData> annotationDataMap;

    /**
     * 类注释信息
     */
    private ApiDocCommentData commentData;


    public String getParamType(){
        return StringUtils.EMPTY;
    }


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


    public AnnotationData get(String annotationName){
        return this.annotationDataMap.get(annotationName);
    }


    /**
     * 根据多个注解名称遍历查询 查询到注解则消费该注解的数据 并退出
     *
     * @param annotationNames        注解名称集合
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


    public Optional<AnnotationData> getAnnotation(String[] annotationNames) {
        if (Objects.isNull(annotationNames) || annotationNames.length > 0) {
            for (String annotationName : annotationNames) {
                Optional<AnnotationData> annotation = getAnnotation(annotationName);
                if (annotation.isPresent()) {
                    return annotation;
                }
            }
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


    /**
     * 校验java类是否为Controller
     *
     * @return true 是一个Controller
     */
    public boolean isController() {
        return exists(AnnotationConstants.CONTROLLER, AnnotationConstants.REST_CONTROLLER);
    }

}
