package com.wdf.fudoc.pojo.data.annotation;

import com.alibaba.fastjson.util.TypeUtils;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注解常量值（注解值为常量）
 * @Date 2022-06-27 20:06:55
 */
@Getter
@Setter
public class AnnotationConstantValueData extends AnnotationValueData {

    /**
     * 常量值
     */
    private Object value;


    /**
     * 获取String类型的值
     */
    public String stringValue() {
        return TypeUtils.castToString(this.value);
    }


    public AnnotationConstantValueData(AnnotationValueType valueType) {
        super(valueType);
    }

    public AnnotationConstantValueData(AnnotationValueType valueType, Object value) {
        super(valueType);
        this.value = value;
    }

}
