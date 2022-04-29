package com.wdf.apidoc.parse;

import com.intellij.psi.PsiParameter;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

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
        if (Objects.nonNull(apiDocCommentData)) {
            return StringUtils.EMPTY;
        }
        return apiDocCommentData.getCommentByParam(getName());
    }
}
