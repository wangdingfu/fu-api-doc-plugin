package com.wdf.apidoc.pojo.data;

import com.wdf.apidoc.constant.enumtype.AnnotationValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 注解值
 * @Date 2022-05-11 21:30:13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationValueData {

    /**
     * 值类型
     */
    private AnnotationValueType valueType;

    /**
     * 值
     */
    private Object value;


    public String getStringValue() {
        if (Objects.isNull(this.valueType) || Objects.isNull(this.value)) {
            return StringUtils.EMPTY;
        }
        if (AnnotationValueType.CONSTANT.equals(this.valueType)) {
            return String.valueOf(value);
        }
        return StringUtils.EMPTY;
    }


}
