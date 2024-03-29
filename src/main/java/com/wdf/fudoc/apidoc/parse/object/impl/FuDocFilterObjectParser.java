package com.wdf.fudoc.apidoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.apidoc.constant.CommonObjectNames;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @description 其他对象解析器(一些框架里不适合解析的对象 例如HttpServletRequest等对象)
 * @date 2022-04-18 21:30:15
 */
public class FuDocFilterObjectParser extends AbstractApiDocObjectParser {

    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.DEFAULT_OBJECT;
    }

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
        return CommonObjectNames.filterList.contains(psiType.getCanonicalText());
    }

    /**
     * java.lang包下的对象为java的类  无法解析 此处直接返回一个默认值在文档处显示
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return java.lang包对象在文档显示的默认值
     */
    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parent) {
        return null;
    }
}
