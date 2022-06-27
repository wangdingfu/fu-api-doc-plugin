package com.wdf.fudoc.pojo.data;

import com.intellij.psi.PsiAnnotation;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private String qualifiedName;

    /**
     * 注解信息
     */
    private PsiAnnotation psiAnnotation;


    /**
     * 注解属性map
     * key:属性名  value:属性值
     */
    private Map<String, AnnotationValueData> attrMap;


    /**
     * 添加注解属性
     *
     * @param attrName 属性名称
     * @param value    属性值
     */
    public void addAttr(String attrName, AnnotationValueData value) {
        if (StringUtils.isNotBlank(attrName) && Objects.nonNull(value)) {
            if (Objects.isNull(attrMap)) {
                this.attrMap = new HashMap<>();
            }
            this.attrMap.put(attrName, value);
        }
    }

    /**
     * 根据属性名称获取常量类型值
     *
     * @param attrName 属性名称
     * @return 常量类型的属性值
     */
    public AnnotationConstantValueData constant(String attrName) {
        AnnotationValueData value = getValue(attrName);
        if (Objects.nonNull(value) && AnnotationValueType.CONSTANT.equals(value.getValueType())) {
            return (AnnotationConstantValueData) value;
        }
        return new AnnotationConstantValueData(AnnotationValueType.CONSTANT, null);
    }

    public AnnotationConstantValueData constant() {
        return constant(FuDocConstants.VALUE);
    }

    /**
     * 根据属性名称获取class类型的值
     *
     * @param attrName 属性名称
     * @return class类型的值
     */
    public AnnotationClassValueData classValue(String attrName) {
        AnnotationValueData value = getValue(attrName);
        if (Objects.nonNull(value) && AnnotationValueType.CLASS.equals(value.getValueType())) {
            return (AnnotationClassValueData) value;
        }
        return new AnnotationClassValueData(AnnotationValueType.CLASS);
    }


    /**
     * 根据属性名称获取枚举类型的值
     *
     * @param attrName 属性名称
     * @return 枚举类型的值
     */
    public AnnotationEnumValueData enumValue(String attrName) {
        AnnotationValueData value = getValue(attrName);
        if (Objects.nonNull(value) && AnnotationValueType.ENUM.equals(value.getValueType())) {
            return (AnnotationEnumValueData) value;
        }
        return new AnnotationEnumValueData(AnnotationValueType.ENUM);
    }


    /**
     * 根据属性名称获取注解类型的值
     *
     * @param attrName 属性名称
     * @return 注解类型的值
     */
    public AnnotationTypeValueData annotationValue(String attrName) {
        AnnotationValueData value = getValue(attrName);
        if (Objects.nonNull(value) && AnnotationValueType.NESTED_ANNOTATION.equals(value.getValueType())) {
            return (AnnotationTypeValueData) value;
        }
        return new AnnotationTypeValueData(AnnotationValueType.NESTED_ANNOTATION);
    }


    /**
     * 根据属性名称获取数组类型值
     *
     * @param attrName 属性名称
     * @return 数组类型值
     */
    public AnnotationArrayValueData arrayValue(String attrName) {
        AnnotationValueData value = getValue(attrName);
        if (Objects.nonNull(value) && AnnotationValueType.ARRAY.equals(value.getValueType())) {
            return (AnnotationArrayValueData) value;
        }
        return new AnnotationArrayValueData(AnnotationValueType.ARRAY);
    }

    public AnnotationArrayValueData arrayValue() {
        return arrayValue(FuDocConstants.VALUE);
    }

    public AnnotationValueData getValue() {
        return getValue(FuDocConstants.VALUE);
    }


    public AnnotationValueData getValue(String attrName) {
        if (Objects.nonNull(this.attrMap) && StringUtils.isNotBlank(attrName)) {
            return this.attrMap.get(attrName);
        }
        return null;
    }


    public AnnotationValueType getValueType() {
        return getValueType(FuDocConstants.VALUE);
    }

    public AnnotationValueType getValueType(String attrName) {
        AnnotationValueData annotationValueData;
        if (Objects.nonNull(this.attrMap) && Objects.nonNull(annotationValueData = this.attrMap.get(attrName))) {
            return annotationValueData.getValueType();
        }
        return AnnotationValueType.CONSTANT;
    }


}
