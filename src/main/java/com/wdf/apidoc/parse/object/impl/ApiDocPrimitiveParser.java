package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;

/**
 * @author wangdingfu
 * @description 基础数据类型解析器
 * @Date 2022-04-18 21:13:00
 */
public class ApiDocPrimitiveParser extends AbstractApiDocObjectParser {


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