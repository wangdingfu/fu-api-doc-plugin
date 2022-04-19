package com.wdf.apidoc.helper;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;

/**
 * @author wangdingfu
 * @descption: 对象类型帮助类
 * @date 2022-04-10 21:41:13
 */
public class ApiDocObjectTypeHelper {


    public static ApiDocObjectTypeBO parse(PsiType psiType) {
        ApiDocObjectTypeBO apiDocObjectTypeBO = new ApiDocObjectTypeBO();
        String canonicalText = psiType.getCanonicalText();
        apiDocObjectTypeBO.setQualifiedName(canonicalText);
        apiDocObjectTypeBO.setCommonObject(true);
        return apiDocObjectTypeBO;
    }




}
