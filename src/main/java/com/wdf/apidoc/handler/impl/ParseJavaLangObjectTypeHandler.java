package com.wdf.apidoc.handler.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;

/**
 * @author wangdingfu
 * @description 解析java.lang包下的对象
 * @Date 2022-04-18 21:30:15
 */
public class ParseJavaLangObjectTypeHandler extends AbstractParseObjectHandler {

    @Override
    public int sort() {
        return 10;
    }

    /**
     * 判断是否为java.lang包下的对象
     *
     * @param psiType 需要解析的对象类型
     * @return true 则需要解析该对象
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return psiType.getCanonicalText().startsWith(CommonClassNames.DEFAULT_PACKAGE);
    }

    /**
     * java.lang包下的对象为java的类  无法解析 此处直接返回一个默认值在文档处显示
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return java.lang包对象在文档显示的默认值
     */
    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectBO parent) {
        return buildDefault(psiType, "---", parent);
    }
}
