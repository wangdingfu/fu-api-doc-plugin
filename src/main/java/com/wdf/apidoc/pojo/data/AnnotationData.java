package com.wdf.apidoc.pojo.data;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiAnnotation;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @descption: 注解属性数据对象
 * @date 2022-04-05 21:16:23
 */
@Getter
@Setter
public class AnnotationData {

    /**
     * 注解全路径
     */
    private String qualifiedName;

    /**
     * 注解信息
     */
    private PsiAnnotation psiAnnotation;


    /**
     * 注解属性map
     * key:属性名  value:属性值
     */
    private Map<String, Object> attrMap;

    public void addAttr(String attrName, Object value) {
        if (StringUtils.isNotBlank(attrName) && Objects.nonNull(value)) {
            if (Objects.isNull(attrMap)) {
                this.attrMap = new HashMap<>();
            }
            this.attrMap.put(attrName, value);
        }
    }


    public String getStringValue(String attr) {
        if (StringUtils.isNotBlank(attr) && Objects.nonNull(this.attrMap)) {
            Object value = this.attrMap.get(attr);
            if (Objects.nonNull(value)) {
                if (value instanceof String) {
                    return (String) value;
                } else if (value instanceof Collection) {
                    System.out.println(value);
                }
            }
        }
        return StringUtils.EMPTY;
    }


}
