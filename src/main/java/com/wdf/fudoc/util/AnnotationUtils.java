package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.lang.jvm.annotation.*;
import com.intellij.psi.PsiAnnotation;
import com.wdf.fudoc.apidoc.pojo.data.annotation.*;
import com.wdf.fudoc.apidoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationValueData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wangdingfu
 * @Descption 注解工具类
 * @date 2022-05-10 21:20:12
 */
public class AnnotationUtils {


    public static String getAnnotationValue(Optional<AnnotationData> annotation, String... attrNames) {
        if (annotation.isPresent()) {
            AnnotationData annotationData = annotation.get();
            if (Objects.nonNull(attrNames) && attrNames.length > 0) {
                for (String attrName : attrNames) {
                    String value = annotationData.constant(attrName).stringValue();
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
                annotationDataMap.put(psiAnnotation.getQualifiedName(), parse(psiAnnotation));
            }
        }
        return annotationDataMap;
    }


    public static AnnotationData parse(PsiAnnotation psiAnnotation) {
        if (Objects.isNull(psiAnnotation)) {
            return null;
        }
        String qualifiedName = psiAnnotation.getQualifiedName();
        AnnotationData annotationData = new AnnotationData();
        annotationData.setQualifiedName(qualifiedName);
        annotationData.setPsiAnnotation(psiAnnotation);
        List<JvmAnnotationAttribute> attributes = psiAnnotation.getAttributes();
        if (CollectionUtils.isNotEmpty(attributes)) {
            for (JvmAnnotationAttribute attribute : attributes) {
                AnnotationValueData annotationValueData = convert(attribute.getAttributeValue());
                if (Objects.nonNull(annotationValueData)) {
                    annotationData.addAttr(attribute.getAttributeName(), annotationValueData);
                }
            }
        }
        return annotationData;
    }


    private static AnnotationValueData convert(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationConstantValue) {
            return convertConstant(((JvmAnnotationConstantValue) attributeValue).getConstantValue());
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


    private static AnnotationConstantValueData convertConstant(Object constantValue) {
        return new AnnotationConstantValueData(AnnotationValueType.CONSTANT, constantValue);
    }


    private static AnnotationEnumValueData convertEnum(JvmAnnotationEnumFieldValue enumFieldValue) {
        return new AnnotationEnumValueData(AnnotationValueType.ENUM, enumFieldValue);
    }

    private static AnnotationClassValueData convertClass(JvmAnnotationClassValue classValue) {
        AnnotationClassValueData annotationClassValueData = new AnnotationClassValueData(AnnotationValueType.CLASS);
        annotationClassValueData.setClassName(classValue.getQualifiedName());
        annotationClassValueData.setJvmAnnotationClassValue(classValue);
        return annotationClassValueData;
    }

    private static AnnotationNestedValueData convertAnnotation(JvmNestedAnnotationValue nestedAnnotationValue) {
        return new AnnotationNestedValueData(AnnotationValueType.NESTED_ANNOTATION);
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
