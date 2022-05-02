package com.wdf.apidoc.helper;

import com.google.common.collect.Lists;
import com.intellij.lang.jvm.annotation.*;
import com.intellij.psi.PsiAnnotation;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @descption: 注解解析帮助类
 * @date 2022-04-05 21:19:50
 */
public class AnnotationParseHelper {


    /**
     * 解析注解
     *
     * @param psiAnnotations 注解集合
     * @return 解析后的注解
     */
    public static Map<String, AnnotationData> parse(PsiAnnotation[] psiAnnotations) {
        Map<String, AnnotationData> annotationDataMap = new HashMap<>();
        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            String qualifiedName = psiAnnotation.getQualifiedName();
            AnnotationData annotationData = new AnnotationData();
            annotationData.setQualifiedName(qualifiedName);
            List<JvmAnnotationAttribute> attributes = psiAnnotation.getAttributes();
            if (CollectionUtils.isNotEmpty(attributes)) {
                for (JvmAnnotationAttribute attribute : attributes) {
                    String attributeName = attribute.getAttributeName();
                    Object value = convertAnnotationAttributeValue(attribute.getAttributeValue());
                    annotationData.addAttr(attributeName, value);
                }
            }
            annotationDataMap.put(qualifiedName, annotationData);
        }
        return annotationDataMap;
    }


    private static Object convertAnnotationAttributeValue(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationEnumFieldValue) {
            return ((JvmAnnotationEnumFieldValue) attributeValue).getFieldName();
        }
        if (attributeValue instanceof JvmAnnotationArrayValue) {
            List<Object> resultList = Lists.newArrayList();
            for (JvmAnnotationAttributeValue value : ((JvmAnnotationArrayValue) attributeValue).getValues()) {
                resultList.add(annotationConstantValue(value));
            }
            return resultList;
        }
        return annotationConstantValue(attributeValue);
    }


    private static Object annotationConstantValue(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationConstantValue) {
            //值为常量
            return ((JvmAnnotationConstantValue) attributeValue).getConstantValue();
        }
        return StringUtils.EMPTY;
    }
}
