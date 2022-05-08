package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @description map对象解析器
 * @Date 2022-04-18 21:33:58
 */
public class ApiDocMapParser extends AbstractApiDocObjectParser {

    @Override
    public int sort() {
        return 200;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_MAP);
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parent) {
        return null;
    }
}
