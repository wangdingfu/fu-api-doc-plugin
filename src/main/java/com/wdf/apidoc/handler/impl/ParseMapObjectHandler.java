package com.wdf.apidoc.handler.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;

/**
 * @author wangdingfu
 * @description map对象解析器
 * @Date 2022-04-18 21:33:58
 */
public class ParseMapObjectHandler extends AbstractParseObjectHandler {

    @Override
    public int sort() {
        return 200;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_MAP);
    }

    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectBO parent) {
        return null;
    }
}
