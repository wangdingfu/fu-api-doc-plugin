package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.lang.jvm.annotation.*;
import com.intellij.psi.PsiAnnotation;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import com.wdf.fudoc.pojo.data.annotation.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @Descption 注解工具类
 * @Date 2022-05-10 21:20:12
 */
public class AnnotationUtils {


    public static String getAnnotationValue(Optional<AnnotationData> annotation, String... attrNames) {
        if (annotation.isPresent()) {
            AnnotationData annotationData = annotation.get();
            if (Objects.nonNull(attrNames) && attrNames.length > 0) {
                for (String attrName : attrNames) {
                    String value = annotationData.constant(attrName).getStringValue();
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解析注解
     *
     * @param psiAnnotations 注解集合
     * @return 解析后的注解
     */
    public static Map<String, AnnotationData> parse(PsiAnnotation[] psiAnnotations) {
        Map<String, AnnotationData> annotationDataMap = new HashMap<>();
        if (Objects.nonNull(psiAnnotations)) {
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                String qualifiedName = psiAnnotation.getQualifiedName();
                AnnotationData annotationData = new AnnotationData();
                annotationData.setQualifiedName(qualifiedName);
                List<JvmAnnotationAttribute> attributes = psiAnnotation.getAttributes();
                if (CollectionUtils.isNotEmpty(attributes)) {
                    for (JvmAnnotationAttribute attribute : attributes) {
                        AnnotationValueData annotationValueData = convert(attribute.getAttributeValue());
                        if (Objects.nonNull(annotationValueData)) {
                            annotationData.addAttr(attribute.getAttributeName(), annotationValueData);
                        }
                    }
                }
                annotationDataMap.put(qualifiedName, annotationData);
            }
        }
        return annotationDataMap;
    }


    private static AnnotationValueData convert(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationConstantValue) {
            return convertConstant((JvmAnnotationConstantValue) attributeValue);
        }
        if (attributeValue instanceof JvmAnnotationClassValue) {
            return convertClass((JvmAnnotationClassValue) attributeValue);
        }
        if (attributeValue instanceof JvmAnnotationEnumFieldValue) {
            return convertEnum((JvmAnnotationEnumFieldValue) attributeValue);
        }
        if (attributeValue instanceof JvmNestedAnnotationValue) {
            return convertAnnotation((JvmNestedAnnotationValue) attributeValue);
        }
        if (attributeValue instanceof JvmAnnotationArrayValue) {
            return convertArray((JvmAnnotationArrayValue) attributeValue);
        }
        return null;
    }


    private static AnnotationConstantValueData convertConstant(JvmAnnotationConstantValue constantValue) {
        return new AnnotationConstantValueData(AnnotationValueType.CONSTANT, constantValue.getConstantValue());
    }


    private static AnnotationEnumValueData convertEnum(JvmAnnotationEnumFieldValue enumFieldValue) {
        return new AnnotationEnumValueData(AnnotationValueType.ENUM);
    }

    private static AnnotationClassValueData convertClass(JvmAnnotationClassValue classValue) {
        return new AnnotationClassValueData(AnnotationValueType.CLASS);
    }

    private static AnnotationTypeValueData convertAnnotation(JvmNestedAnnotationValue nestedAnnotationValue) {
        return new AnnotationTypeValueData(AnnotationValueType.NESTED_ANNOTATION);
    }


    private static AnnotationArrayValueData convertArray(JvmAnnotationArrayValue arrayValue) {
        AnnotationArrayValueData arrayValueData = new AnnotationArrayValueData(AnnotationValueType.ARRAY);
        List<JvmAnnotationAttributeValue> arrayValueValues = arrayValue.getValues();
        List<AnnotationValueData> values = Lists.newArrayList();
        for (JvmAnnotationAttributeValue value : arrayValueValues) {
            values.add(convert(value));
        }
        arrayValueData.setValues(values);
        return arrayValueData;
    }

}
