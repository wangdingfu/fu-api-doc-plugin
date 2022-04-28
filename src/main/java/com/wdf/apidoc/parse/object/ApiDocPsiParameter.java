package com.wdf.apidoc.parse.object;

import com.intellij.psi.PsiParameter;
import com.wdf.apidoc.parse.AbstractApiDocField;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;

/**
 * @author wangdingfu
 * @Descption
 * @Date 2022-04-28 22:25:06
 */
public class ApiDocPsiParameter extends AbstractApiDocField {

    private final PsiParameter psiParameter;

    private final ApiDocCommentData apiDocCommentData;

    public ApiDocPsiParameter(PsiParameter psiParameter, ApiDocCommentData apiDocCommentData) {
        this.psiParameter = psiParameter;
        this.apiDocCommentData = apiDocCommentData;
    }

    @Override
    public String getName() {
        return psiParameter.getName();
    }

    @Override
    public String getComment() {
        return apiDocCommentData.getCommentByParam(getName());
    }
}
