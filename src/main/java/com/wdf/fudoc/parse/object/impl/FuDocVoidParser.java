package com.wdf.fudoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.PsiClassUtils;

/**
 * @author wangdingfu
 * @Descption Void类型解析器
 * @Date 2022-07-05 16:34:39
 */
public class FuDocVoidParser  extends AbstractApiDocObjectParser {
    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.VOID;
    }

    @Override
    public int sort() {
        return 100;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return PsiClassUtils.isVoid(psiType);
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        return null;
    }
}
