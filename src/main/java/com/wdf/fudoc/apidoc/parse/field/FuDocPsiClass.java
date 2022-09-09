package com.wdf.fudoc.apidoc.parse.field;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierList;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: psiClass参数实现类
 * @date 2022-05-04 23:36:23
 */
public class FuDocPsiClass extends AbstractFuDocField {

    private final PsiClass psiClass;


    private final ApiDocCommentData apiDocCommentData;

    public FuDocPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
        this.apiDocCommentData = DocCommentParseHelper.parseComment(psiClass.getDocComment());
    }

    public FuDocPsiClass(PsiClass psiClass, ApiDocCommentData apiDocCommentData) {
        this.psiClass = psiClass;
        this.apiDocCommentData = apiDocCommentData;
    }

    @Override
    public String getName() {
        return this.psiClass.getName();
    }

    @Override
    public String getComment() {
        if (Objects.isNull(apiDocCommentData)) {
            return StringUtils.EMPTY;
        }
        return apiDocCommentData.getCommentByParam(getName());
    }

    @Override
    public PsiAnnotation[] getAnnotations() {
        return this.psiClass.getAnnotations();
    }

    @Override
    public boolean hasProperty(String name) {
        PsiModifierList modifierList = this.psiClass.getModifierList();
        if (Objects.nonNull(modifierList)) {
            return modifierList.hasModifierProperty(name);
        }
        return false;
    }
}
