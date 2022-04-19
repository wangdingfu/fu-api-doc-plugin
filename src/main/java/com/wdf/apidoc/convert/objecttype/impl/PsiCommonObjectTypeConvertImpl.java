package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;
import com.wdf.apidoc.enumtype.CommonObjectType;
import com.wdf.apidoc.enumtype.PsiObjectType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @descption: 常用的公共对象转换
 * @date 2022-04-16 20:14:37
 */
public class PsiCommonObjectTypeConvertImpl extends AbstractPsiObjectTypeConvert {


    @Override
    public int sort() {
        return 1;
    }

    /**
     * 判断是否需要解析公共对象
     *
     * @param psiType 对象类型
     * @return 是否需要解析该类型
     */
    @Override
    public boolean isMatch(PsiType psiType) {
        return StringUtils.isNotBlank(CommonObjectType.getName(psiType.getCanonicalText()));
    }


    /**
     * 解析公共对象
     *
     * @param psiType 对象类型
     * @return 解析后的描述对象
     */
    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        return build(psiType, PsiObjectType.COMMON, CommonObjectType.getName(psiType.getCanonicalText()));
    }
}
