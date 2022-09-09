package com.wdf.fudoc.apidoc.pojo.data.annotation;

import com.wdf.fudoc.apidoc.constant.enumtype.AnnotationValueType;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationValueData;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @Descption 注解数组类型值对象
 * @date 2022-06-27 20:46:04
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

    public ArrayClassValue clazz() {
        return new ArrayClassValue(ObjectUtils.listToList(values, data -> (AnnotationClassValueData) data));
    }

    public ArrayEnumValue enumValue() {
        return new ArrayEnumValue(ObjectUtils.listToList(values, data -> (AnnotationEnumValueData) data));
    }


    public ArrayNestedAnnotationValue nested() {
        return new ArrayNestedAnnotationValue(ObjectUtils.listToList(values, data -> (AnnotationNestedValueData) data));
    }


    public static class ArrayConstantValue {
        private final List<AnnotationConstantValueData> valueDataList;

        public ArrayConstantValue(List<AnnotationConstantValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }

        public List<String> stringValue() {
            return valueDataList.stream().map(AnnotationConstantValueData::stringValue).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        }
    }


    public static class ArrayClassValue {
        private final List<AnnotationClassValueData> valueDataList;

        ArrayClassValue(List<AnnotationClassValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }

        public List<String> className() {
            return valueDataList.stream().map(AnnotationClassValueData::getClassName).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        }
    }


    public static class ArrayEnumValue {
        private final List<AnnotationEnumValueData> valueDataList;

        ArrayEnumValue(List<AnnotationEnumValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }
    }


    public static class ArrayNestedAnnotationValue {
        private final List<AnnotationNestedValueData> valueDataList;

        ArrayNestedAnnotationValue(List<AnnotationNestedValueData> valueDataList) {
            this.valueDataList = valueDataList;
        }
    }
}
