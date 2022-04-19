package com.wdf.apidoc.convert.paramter;

import com.intellij.psi.PsiParameter;
import com.wdf.apidoc.context.ApiDocContext;
import com.wdf.apidoc.data.ApiDocObjectData;

/**
 * @author wangdingfu
 * @descption: 参数转换接口
 * @date 2022-04-10 20:44:21
 */
interface PsiParameterConvert {


    /**
     * 转换参数
     *
     * @param apiDocContext 上下文
     * @param psiParameter  参数对象
     * @return 转换后的API参数属性数据对象
     */
    ApiDocObjectData convert(ApiDocContext apiDocContext, PsiParameter psiParameter, String docText);
}