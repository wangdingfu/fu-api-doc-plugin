package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.constant.CommonObjectNames;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @Descption 文件对象解析
 * @Date 2022-06-08 21:51:18
 */
public class FuDocMultipartFileParser extends AbstractApiDocObjectParser {
    @Override
    protected ApiDocObjectType getObjectType() {
        return ApiDocObjectType.MULTIPART_FILE;
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
