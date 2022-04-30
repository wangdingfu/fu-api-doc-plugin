package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


}
