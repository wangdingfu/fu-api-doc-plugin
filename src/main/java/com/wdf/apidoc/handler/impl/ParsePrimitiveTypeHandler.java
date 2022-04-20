package com.wdf.apidoc.handler.impl;

import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;

/**
 * @author wangdingfu
 * @Descption 解析基础数据类型
 * @Date 2022-04-18 21:13:00
 */
public class ParsePrimitiveTypeHandler extends AbstractParseObjectHandler {


    /**
     * 执行顺序
     *
     * @return 默认第一个执行
     */
    @Override
    public int sort() {
        return Integer.MIN_VALUE;
    }

    /**
     * 判断该类型是否为java的8种基本数据类型
     *
     * @param psiType 需要解析的对象类型
     * @return true 是  false 不是
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return psiType instanceof PsiPrimitiveType;
    }


    /**
     * 将java中的基本数据类型解析成ApiDoc对象
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return 描述基本数据类型的文档信息对象
     */
    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectBO parent) {
        return buildDefault(psiType, psiType.getCanonicalText(), parent);
    }
}
