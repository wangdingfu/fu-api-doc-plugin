package com.wdf.fudoc.pojo.data.annotation;

import com.intellij.lang.jvm.JvmClass;
import com.intellij.lang.jvm.annotation.JvmAnnotationClassValue;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解class类型值（注解值为class）
 * @Date 2022-06-27 20:07:36
 */
@Getter
@Setter
public class AnnotationClassValueData extends AnnotationValueData {

    /**
     * class包路径
     */
    private String className;

    private JvmAnnotationClassValue jvmAnnotationClassValue;

    public AnnotationClassValueData(AnnotationValueType valueType) {
        super(valueType);
    }
}
