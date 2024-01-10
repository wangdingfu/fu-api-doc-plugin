package com.wdf.fudoc.apidoc.pojo.data.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 注解常量值（注解值为常量）
 * @date 2022-06-27 20:06:55
 */
@Getter
@Setter
public class AnnotationConstantValueData extends AnnotationValueData {

    /**
     * 常量值
     */
    private Object value;


    public Boolean booleanValue() {
        return (Boolean) this.value;
    }


    /**
     * 获取String类型的值
     */
    public String stringValue() {
        return Objects.isNull(this.value) ? FuStringUtils.EMPTY : this.value.toString();
    }


    public AnnotationConstantValueData(AnnotationValueType valueType) {
        super(valueType);
    }

    public AnnotationConstantValueData(AnnotationValueType valueType, Object value) {
        super(valueType);
        this.value = value;
    }

}
