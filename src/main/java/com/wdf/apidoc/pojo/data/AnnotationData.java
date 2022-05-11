package com.wdf.apidoc.pojo.data;

import com.intellij.psi.PsiAnnotation;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.constant.enumtype.AnnotationValueType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
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

    public void addAttr(String attrName, AnnotationValueData value) {
        if (StringUtils.isNotBlank(attrName) && Objects.nonNull(value)) {
            if (Objects.isNull(attrMap)) {
                this.attrMap = new HashMap<>();
            }
            this.attrMap.put(attrName, value);
        }
    }


    public AnnotationValueType getValueType() {
        return getValueType(ApiDocConstants.VALUE);
    }

    public AnnotationValueType getValueType(String attrName) {
        return AnnotationValueType.CONSTANT;
    }


    public String getStringValue(String attr) {
        if (StringUtils.isNotBlank(attr) && Objects.nonNull(this.attrMap)) {
            Object value = this.attrMap.get(attr);
            if (Objects.nonNull(value)) {
                if (value instanceof String) {
                    return (String) value;
                } else if (value instanceof Collection) {
                    System.out.println(value);
                }
            }
        }
        return StringUtils.EMPTY;
    }


}
