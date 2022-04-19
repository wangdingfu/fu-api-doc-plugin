package com.wdf.apidoc.handler.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectParentBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;

/**
 * @author wangdingfu
 * @Descption 对象解析器(用户自定义对象)
 * @Date 2022-04-18 21:34:42
 */
public class ParseDefaultObjectHandler extends AbstractParseObjectHandler {


    /**
     * 默认最后解析  只有所有的解析器都无法解析时 才会走当前的解析器
     *
     * @return 返回最大值 即将当前解析器排序在最后一位
     */
    @Override
    public int sort() {
        return Integer.MIN_VALUE;
    }

    /**
     * 永远返回true 如果前面所有的解析器都不能解析 则当前解析器负责解析
     *
     * @param psiType 需要解析的对象类型
     * @return 默认解析所有对象
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return true;
    }


    /**
     * 解析对象（一般为用户自定义对象）
     * 只有当其他所有解析器无法解析时 才会走当前解析器来解析
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return 解析后的ApiDoc对象
     */
    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectParentBO parent) {
        return null;
    }
}
