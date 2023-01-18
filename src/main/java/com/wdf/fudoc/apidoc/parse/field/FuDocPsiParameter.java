package com.wdf.fudoc.apidoc.parse.field;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption
 * @date 2022-04-28 22:25:06
 */
public class FuDocPsiParameter extends AbstractFuDocField {

    private final PsiParameter psiParameter;

    private final ApiDocCommentData apiDocCommentData;

    public FuDocPsiParameter(PsiParameter psiParameter, ApiDocCommentData apiDocCommentData) {
        this.psiParameter = psiParameter;
        this.apiDocCommentData = apiDocCommentData;
    }

    /**
     * 获取参数名
     */
    @Override
    public String getName() {
        if (Objects.isNull(psiParameter)) {
            return StringUtils.EMPTY;
        }
        return psiParameter.getName();
    }


    /**
     * 获取参数在方法上的注释
     */
    @Override
    public String getComment() {
        if (Objects.isNull(apiDocCommentData)) {
            return StringUtils.EMPTY;
        }
        return apiDocCommentData.getCommentByParam(getName());
    }

    /**
     * 获取参数上的注解
     */
    @Override
    public PsiAnnotation[] getAnnotations() {
        return psiParameter.getAnnotations();
    }

    /**
     * 字段是否被指定修饰符修饰
     *
     * @param name 修饰关键字 例如 static final
     * @return true 存在
     */
    @Override
    public boolean hasProperty(String name) {
        PsiModifierList modifierList = this.psiParameter.getModifierList();
        if (Objects.nonNull(modifierList)) {
            return modifierList.hasModifierProperty(name);
        }
        return false;
    }
}
