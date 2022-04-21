package com.wdf.apidoc.pojo.bo;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析对象的父级对象信息bo
 * @Date 2022-04-18 20:58:23
 */
@Getter
@Setter
public class ParseObjectBO {

    /**
     * 字段
     */
    private PsiField psiField;

    /**
     * 请求参数
     */
    private PsiParameter psiParameter;

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


    public String getName() {
        if (Objects.nonNull(this.psiField)) {
            return this.psiField.getName();
        }
        if (Objects.nonNull(psiParameter)) {
            return this.psiParameter.getName();
        }
        return StringUtils.EMPTY;
    }


    public String getDocText() {
        if (Objects.nonNull(this.psiField)) {
            PsiDocComment docComment = this.psiField.getDocComment();
            if (Objects.nonNull(docComment)) {
                return docComment.getText();
            }
        }
        return StringUtils.EMPTY;
    }

}
