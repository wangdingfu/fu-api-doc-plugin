package com.wdf.fudoc.pojo.data;

import com.intellij.psi.PsiAnnotation;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解值
 * @Date 2022-05-11 21:30:13
 */
@Getter
@Setter
public class AnnotationValueData {

    /**
     * 值类型
     */
    private AnnotationValueType valueType;

    public AnnotationValueData(AnnotationValueType valueType) {
        this.valueType = valueType;
    }


}
