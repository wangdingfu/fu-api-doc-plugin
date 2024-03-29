package com.wdf.fudoc.apidoc.pojo.data;

import com.wdf.fudoc.apidoc.constant.enumtype.AnnotationValueType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解值
 * @date 2022-05-11 21:30:13
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
