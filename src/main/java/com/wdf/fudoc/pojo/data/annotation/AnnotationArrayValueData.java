package com.wdf.fudoc.pojo.data.annotation;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 注解数组类型值对象
 * @Date 2022-06-27 20:46:04
 */
@Getter
@Setter
public class AnnotationArrayValueData extends AnnotationValueData {

    public AnnotationArrayValueData(AnnotationValueType valueType) {
        super(valueType);
    }

    /**
     * 数组中的属性值类型(默认数组里所有属性值都必须为同一类型)
     */
    private AnnotationValueType itemValueType;
    /**
     * 属性值集合
     */
    private List<AnnotationValueData> values;


    public List<String> constantStringValue() {
        List<String> resultList = Lists.newArrayList();
        if (Objects.nonNull(values)) {
            for (AnnotationValueData value : values) {
                if (AnnotationValueType.CONSTANT.equals(value.getValueType())) {
                    resultList.add(((AnnotationConstantValueData) value).getStringValue());
                }
            }
        }
        return resultList;
    }
}
