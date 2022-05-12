package com.wdf.apidoc.pojo.data;

import com.intellij.psi.PsiAnnotation;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.constant.enumtype.AnnotationValueType;
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

    public void addAttr(String attrName, AnnotationValueData value) {
        if (StringUtils.isNotBlank(attrName) && Objects.nonNull(value)) {
            if (Objects.isNull(attrMap)) {
                this.attrMap = new HashMap<>();
            }
            this.attrMap.put(attrName, value);
        }
    }

    public AnnotationValueData getValue() {
        return getValue(ApiDocConstants.VALUE);
    }


    public AnnotationValueData getValue(String attrName) {
        if (Objects.nonNull(this.attrMap) && StringUtils.isNotBlank(attrName)) {
            return this.attrMap.get(attrName);
        }
        return new AnnotationValueData();
    }


    public AnnotationValueType getValueType() {
        return getValueType(ApiDocConstants.VALUE);
    }

    public AnnotationValueType getValueType(String attrName) {
        AnnotationValueData annotationValueData;
        if (Objects.nonNull(this.attrMap) && Objects.nonNull(annotationValueData = this.attrMap.get(attrName))) {
            return annotationValueData.getValueType();
        }
        return AnnotationValueType.CONSTANT;
    }


}
