package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;

/**
 * @author wangdingfu
 * @descption: java对象转换实现类(当一个对象没有被其他转换器转换 则当前转换器将会把该对象当成一个object对象 遍历对象的所有属性解析)
 * @date 2022-04-17 17:41:14
 */
public class DefaultObjectTypeConvertImpl extends AbstractPsiObjectTypeConvert {

    @Override
    public int sort() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isMatch(PsiType psiType) {
        return true;
    }

    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        return null;
    }
}
