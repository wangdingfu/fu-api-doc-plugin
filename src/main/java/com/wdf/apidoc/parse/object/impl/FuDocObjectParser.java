package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Descption Object对象解析器
 * @Date 2022-06-08 22:41:13
 */
public class FuDocObjectParser extends AbstractApiDocObjectParser {
    @Override
    protected ApiDocObjectType getObjectType() {
        return ApiDocObjectType.OBJECT;
    }

    @Override
    public int sort() {
        return 99;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return CommonObjectType.OBJECT_TYPE.getObjPkg().equals(psiType.getCanonicalText());
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        return buildDefault(psiType, "object", parseObjectBO);
    }
}
