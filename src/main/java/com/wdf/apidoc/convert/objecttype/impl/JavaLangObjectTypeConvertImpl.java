package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;
import com.wdf.apidoc.enumtype.PsiObjectType;

/**
 * @author wangdingfu
 * @descption: java.lang包下的对象转换器
 * @date 2022-04-16 21:20:41
 */
public class JavaLangObjectTypeConvertImpl extends AbstractPsiObjectTypeConvert {

    @Override
    public int sort() {
        return 2;
    }

    @Override
    public boolean isMatch(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return canonicalText.startsWith(CommonClassNames.DEFAULT_PACKAGE);
    }

    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        return build(psiType, PsiObjectType.COMMON,"---");
    }
}
