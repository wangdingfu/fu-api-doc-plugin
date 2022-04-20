package com.wdf.apidoc.handler.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.enumtype.CommonObjectType;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @Descption 解析公共java对象
 * @Date 2022-04-18 21:26:53
 */
public class ParseCommonObjectTypeHandler extends AbstractParseObjectHandler {

    /**
     * 执行顺序
     *
     * @return 第二个执行
     */
    @Override
    public int sort() {
        return 1;
    }

    /**
     * 判断是否为java的公共对象 即包装类或者常用类
     *
     * @param psiType 需要解析的对象类型
     * @return 返回true则需要调用parse方法解析该对象
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return StringUtils.isNotBlank(CommonObjectType.getName(psiType.getCanonicalText()));
    }


    /**
     * 解析java公共对象成ApiDoc文档数据对象
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return ApiDoc文档数据对象
     */
    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectBO parent) {
        return buildDefault(psiType, CommonObjectType.getName(psiType.getCanonicalText()), parent);
    }
}
