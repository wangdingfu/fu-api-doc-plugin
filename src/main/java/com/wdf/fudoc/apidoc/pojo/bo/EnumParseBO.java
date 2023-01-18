package com.wdf.fudoc.apidoc.pojo.bo;

import com.intellij.psi.PsiParameter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-07-29 17:03:43
 */
@Getter
@Setter
public class EnumParseBO {

    private PsiParameter psiParameter;

    private String type;

    private String name;

    private String value;

    public EnumParseBO(PsiParameter psiParameter, String value) {
        this.psiParameter = psiParameter;
        this.type = psiParameter.getType().getCanonicalText();
        this.name = psiParameter.getName();
        this.value = value;
    }
}
