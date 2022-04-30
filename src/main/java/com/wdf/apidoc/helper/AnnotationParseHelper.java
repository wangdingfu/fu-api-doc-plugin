package com.wdf.apidoc.helper;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationEnumFieldValue;
import com.intellij.psi.PsiAnnotation;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationData;
import org.apache.commons.collections.CollectionUtils;

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
     * @param apiDocContext  全局上下文
     * @param psiAnnotations 注解集合
     * @return 解析后的注解
     */
    public static Map<String, AnnotationData> parse(ApiDocContext apiDocContext, PsiAnnotation[] psiAnnotations) {
        Map<String, AnnotationData> annotationDataMap = new HashMap<>();
        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            String qualifiedName = psiAnnotation.getQualifiedName();
            List<JvmAnnotationAttribute> attributes = psiAnnotation.getAttributes();
            if (CollectionUtils.isNotEmpty(attributes)) {
                for (JvmAnnotationAttribute attribute : attributes) {
                    String attributeName = attribute.getAttributeName();
                    Object value = convertAnnotationAttributeValue(attribute.getAttributeValue());
                    AnnotationData annotationData = new AnnotationData();
                    annotationData.setQualifiedName(qualifiedName);
                    annotationData.addAttr(attributeName, value);
                    annotationDataMap.put(qualifiedName, annotationData);
                }
            }
        }
        return annotationDataMap;
    }


    private static Object convertAnnotationAttributeValue(JvmAnnotationAttributeValue attributeValue) {
        if (attributeValue instanceof JvmAnnotationConstantValue) {
            //值为常量
            return ((JvmAnnotationConstantValue) attributeValue).getConstantValue();
        }
        if (attributeValue instanceof JvmAnnotationEnumFieldValue) {
            return ((JvmAnnotationEnumFieldValue) attributeValue).getField();
        }
        return null;
    }
}
