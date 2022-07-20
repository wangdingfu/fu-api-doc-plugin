package com.wdf.fudoc.service;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.pojo.context.FuDocContext;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:45:00
 */
public interface FuDocService {

    /**
     * 生成对应类型的fuDoc
     *
     * @return markdown格式的接口文档内容
     */
    String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass);
}
