package com.wdf.fudoc.apidoc.parse.field;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.wdf.fudoc.apidoc.constant.enumtype.CommentTagType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.helper.EnumParseHelper;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption psiField字段
 * @date 2022-04-28 22:20:47
 */
public class FuDocPsiField extends AbstractFuDocField {


    /**
     * psiField字段类型
     */
    private final PsiField psiField;

    public FuDocPsiField(PsiField psiField) {
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
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiField);
        PsiElement psiElement = apiDocCommentData.getTagComment(CommentTagType.SEE.getName()).getPsiElement();
        PsiClass psiClass;
        if (Objects.nonNull(psiElement) && psiElement instanceof PsiClass && (psiClass = (PsiClass) psiElement).isEnum()) {
            //如果是枚举 则解析枚举
            String enumContent = EnumParseHelper.parseEnum(psiClass, YesOrNo.YES.getCode());
            return apiDocCommentData.getCommentTitle() + " " + StringUtils.replace(enumContent, "\r\n", "");
        }
        return apiDocCommentData.getCommentTitle();
    }

    /**
     * 获取字段上的注解
     */
    @Override
    public PsiAnnotation[] getAnnotations() {
        return psiField.getAnnotations();
    }

    @Override
    public String getParamType() {
        return psiField.getType().getPresentableText();
    }

    /**
     * 字段是否被指定修饰符修饰
     *
     * @param name 修饰关键字 例如 static final
     * @return true 存在
     */
    @Override
    public boolean hasProperty(String name) {
        PsiModifierList modifierList = this.psiField.getModifierList();
        if (Objects.nonNull(modifierList)) {
            return modifierList.hasModifierProperty(name);
        }
        return false;
    }
}
