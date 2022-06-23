package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.lang.jvm.annotation.*;
import com.intellij.psi.PsiAnnotation;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
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
                    String value = annotationData.getValue(attrName).getStringValue();
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
                        String attributeName = attribute.getAttributeName();
                        AnnotationValueData value = convertAnnotationAttributeValue(attribute.getAttributeValue());
                        annotationData.addAttr(attributeName, value);
                    }
                }
                annotationDataMap.put(qualifiedName, annotationData);
            }
        }
        return annotationDataMap;
    }


    private static AnnotationValueData convertAnnotationAttributeValue(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationEnumFieldValue) {
            //值类型为:JvmAnnotationEnumFieldValue
            return new AnnotationValueData(AnnotationValueType.ENUM, attributeValue);
        }
        if (attributeValue instanceof JvmAnnotationArrayValue) {
            List<Object> resultList = Lists.newArrayList();
            for (JvmAnnotationAttributeValue value : ((JvmAnnotationArrayValue) attributeValue).getValues()) {
                resultList.add(annotationConstantValue(value));
            }
            return new AnnotationValueData(AnnotationValueType.ARRAY, resultList);
        }
        return new AnnotationValueData(AnnotationValueType.CONSTANT, annotationConstantValue(attributeValue));
    }


    private static Object annotationConstantValue(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationConstantValue) {
            //值为常量
            return ((JvmAnnotationConstantValue) attributeValue).getConstantValue();
        }
        return StringUtils.EMPTY;
    }
}
