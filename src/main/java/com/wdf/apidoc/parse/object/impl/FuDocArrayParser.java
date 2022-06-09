package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @descption: 数组解析器
 * @date 2022-05-22 20:22:10
 */
public class FuDocArrayParser extends AbstractApiDocObjectParser {
    @Override
    protected ApiDocObjectType getObjectType() {
        return ApiDocObjectType.ARRAY;
    }

    @Override
    public int sort() {
        return 99;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return psiType instanceof PsiArrayType;
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        return buildDefault(psiType, psiType.getPresentableText(), parseObjectBO);
    }
}
