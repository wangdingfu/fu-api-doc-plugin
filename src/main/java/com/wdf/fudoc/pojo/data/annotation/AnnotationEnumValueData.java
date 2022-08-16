package com.wdf.fudoc.pojo.data.annotation;

import com.intellij.lang.jvm.JvmEnumField;
import com.intellij.lang.jvm.annotation.JvmAnnotationEnumFieldValue;
import com.intellij.psi.PsiField;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 注解枚举类型值（注解值为枚举类型）
 * @date 2022-06-27 20:08:37
 */
@Getter
@Setter
public class AnnotationEnumValueData extends AnnotationValueData {

    /**
     * 枚举类路径
     */
    private String enumClassName;
    /**
     * 枚举值
     */
    private String enumValue;

    /**
     * 枚举字段
     */
    private JvmEnumField enumField;

    public AnnotationEnumValueData(AnnotationValueType valueType) {
        super(valueType);
    }

    public AnnotationEnumValueData(AnnotationValueType valueType, JvmAnnotationEnumFieldValue enumFieldValue) {
        super(valueType);
        if (Objects.nonNull(enumFieldValue)) {
            this.enumField = enumFieldValue.getField();
            this.enumClassName = enumFieldValue.getContainingClass().getQualifiedName();
            if (Objects.nonNull(this.enumField)) {
                this.enumValue = this.enumField.getName();
            }
        }
    }


}
