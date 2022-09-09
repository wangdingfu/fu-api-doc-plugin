package com.wdf.fudoc.apidoc.pojo.data.annotation;

import com.intellij.lang.jvm.annotation.JvmAnnotationClassValue;
import com.wdf.fudoc.apidoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解class类型值（注解值为class）
 * @date 2022-06-27 20:07:36
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
