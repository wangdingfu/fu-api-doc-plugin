package com.wdf.fudoc.pojo.data.annotation;

import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解枚举类型值（注解值为枚举类型）
 * @date 2022-06-27 20:08:37
 */
@Getter
@Setter
public class AnnotationEnumValueData extends AnnotationValueData {

    private String value;

    public AnnotationEnumValueData(AnnotationValueType valueType) {
        super(valueType);
    }
}
