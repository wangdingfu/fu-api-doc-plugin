package com.wdf.apidoc.pojo.bo;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.apidoc.parse.ApiDocField;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author wangdingfu
 * @description 解析对象的父级对象信息bo
 * @Date 2022-04-18 20:58:23
 */
@Getter
@Setter
public class ParseObjectBO {

    /**
     * 上下文对象
     */
    private ApiDocContext apiDocContext;

    /**
     * 字段
     */
    private ApiDocField apiDocField;

    /**
     * 泛型map
     */
    private Map<String, PsiType> genericsMap;


    public PsiType getPsiType(String generics) {
        if (MapUtils.isNotEmpty(genericsMap) && StringUtils.isNotBlank(generics)) {
            return genericsMap.get(generics);
        }
        return null;
    }
}
