package com.wdf.fudoc.pojo.data.annotation;

import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解类型值（即值的类型为注解）
 * @Date 2022-06-27 20:17:39
 */
@Getter
@Setter
public class AnnotationNestedValueData extends AnnotationValueData {

    public AnnotationNestedValueData(AnnotationValueType valueType) {
        super(valueType);
    }
}
