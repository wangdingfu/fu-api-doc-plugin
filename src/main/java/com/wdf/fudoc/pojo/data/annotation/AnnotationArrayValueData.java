package com.wdf.fudoc.pojo.data.annotation;

import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.pojo.data.AnnotationValueData;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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


    public ArrayConstantValue constant() {
        return new ArrayConstantValue(ObjectUtils.listToList(values, data -> (AnnotationConstantValueData) data));
    }

    public ClassValue clazz() {
        return new ClassValue(ObjectUtils.listToList(values, data -> (AnnotationClassValueData) data));
    }

    public EnumValue enumValue() {
        return new EnumValue(ObjectUtils.listToList(values, data -> (AnnotationEnumValueData) data));
    }


    public NestedAnnotationValue nested() {
        return new NestedAnnotationValue(ObjectUtils.listToList(values, data -> (AnnotationNestedValueData) data));
    }


    public static class ArrayConstantValue {
        private final List<AnnotationConstantValueData> valueDataList;

        public ArrayConstantValue(List<AnnotationConstantValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }

        public List<String> stringValue() {
            return valueDataList.stream().map(AnnotationConstantValueData::stringValue).filter(StringUtils::isEmpty).collect(Collectors.toList());
        }
    }


    public static class ClassValue {
        private final List<AnnotationClassValueData> valueDataList;

        ClassValue(List<AnnotationClassValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }

        public List<String> className() {
            return valueDataList.stream().map(AnnotationClassValueData::getClassName).filter(StringUtils::isEmpty).collect(Collectors.toList());
        }
    }


    public static class EnumValue {
        private final List<AnnotationEnumValueData> valueDataList;

        EnumValue(List<AnnotationEnumValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }
    }


    public static class NestedAnnotationValue {
        private final List<AnnotationNestedValueData> valueDataList;

        NestedAnnotationValue(List<AnnotationNestedValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }
    }
}
