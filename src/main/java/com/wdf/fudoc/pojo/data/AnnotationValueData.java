package com.wdf.fudoc.pojo.data;

import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.enumtype.AnnotationValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

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


    public List<String> getListValue() {
        if (AnnotationValueType.CONSTANT.equals(valueType)) {
            return Lists.newArrayList(getStringValue());
        }
        List<String> resultList = Lists.newArrayList();
        if (AnnotationValueType.ARRAY.equals(this.valueType)) {
            for (Object o : ((List<?>) this.value)) {
                resultList.add(TypeUtils.castToString(o));
            }
        }
        resultList.removeAll(Collections.singleton(null));
        return resultList;
    }


    public List<Class<?>> getListClassValue() {
        if (AnnotationValueType.CLASS.equals(valueType)) {
            return Lists.newArrayList(getClassValue(this.value));
        }
        List<Class<?>> resultList = Lists.newArrayList();
        if (AnnotationValueType.ARRAY.equals(this.valueType)) {
            for (Object o : ((List<?>) this.value)) {
                resultList.add(getClassValue(o));
            }
        }
        resultList.removeAll(Collections.singleton(null));
        return resultList;
    }


    public String getStringValue() {
        return TypeUtils.castToString(this.value);
    }



    public Class<?> getClassValue(Object value) {
        return (Class<?>) value;
    }

}
