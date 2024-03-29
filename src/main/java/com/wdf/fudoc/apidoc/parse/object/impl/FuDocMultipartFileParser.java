package com.wdf.fudoc.apidoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.apidoc.constant.CommonObjectNames;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Descption 文件对象解析
 * @date 2022-06-08 21:51:18
 */
public class FuDocMultipartFileParser extends AbstractApiDocObjectParser {
    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.MULTIPART_FILE;
    }

    @Override
    public int sort() {
        return 200;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonObjectNames.MULTIPART_FILE.equals(canonicalText);
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        return buildDefault(psiType, "file", parseObjectBO);
    }
}
