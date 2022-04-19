package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;

/**
 * @author wangdingfu
 * @descption: map集合对象转换
 * @date 2022-04-16 21:19:27
 */
public class PsiMapTypeConvertImpl extends AbstractPsiObjectTypeConvert {


    @Override
    public int sort() {
        return 200;
    }

    @Override
    public boolean isMatch(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_MAP);
    }

    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        return null;
    }
}
