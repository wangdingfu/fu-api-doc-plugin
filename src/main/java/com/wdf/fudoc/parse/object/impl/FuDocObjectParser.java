package com.wdf.fudoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.constant.enumtype.CommonObjectType;
import com.wdf.fudoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Descption Object对象解析器
 * @Date 2022-06-08 22:41:13
 */
public class FuDocObjectParser extends AbstractApiDocObjectParser {
    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.OBJECT;
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
