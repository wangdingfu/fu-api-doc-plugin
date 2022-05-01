package com.wdf.apidoc.parse;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.apidoc.helper.DocCommentParseHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption psiField字段
 * @Date 2022-04-28 22:20:47
 */
public class ApiDocPsiField extends AbstractApiDocField {


    /**
     * psiField字段类型
     */
    private final PsiField psiField;

    public ApiDocPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    /**
     * 获取参数名|字段名
     */
    @Override
    public String getName() {
        return psiField.getName();
    }


    /**
     * 获取参数字段注释
     */
    @Override
    public String getComment() {
        PsiDocComment docComment = psiField.getDocComment();
        if (Objects.nonNull(docComment)) {
            return DocCommentParseHelper.getCommentContent(docComment);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取字段上的注解
     */
    @Override
    public PsiAnnotation[] getAnnotations() {
        return psiField.getAnnotations();
    }
}
