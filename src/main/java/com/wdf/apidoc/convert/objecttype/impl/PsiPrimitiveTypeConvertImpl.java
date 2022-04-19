package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;
import com.wdf.apidoc.enumtype.PsiObjectType;

/**
 * @author wangdingfu
 * @descption: 基本数据类型转换
 * @date 2022-04-16 20:13:40
 */
public class PsiPrimitiveTypeConvertImpl extends AbstractPsiObjectTypeConvert {

    @Override
    public int sort() {
        return Integer.MIN_VALUE;
    }

    /**
     * 判断是否为基础数据类型
     *
     * @param psiType 对象类型
     * @return 是否需要解析该类型
     */
    @Override
    public boolean isMatch(PsiType psiType) {
        return psiType instanceof PsiPrimitiveType;
    }


    /**
     * 返回java基础数据类型的描述结构信息
     *
     * @param psiType 对象类型
     * @return 基础数据类型描述信息
     */
    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        return build(psiType, PsiObjectType.PRIMITIVE, psiType.getCanonicalText());
    }
}
