package com.wdf.fudoc.service;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.pojo.context.FuDocContext;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:52:37
 */
public class GenObjectFuDocServiceImpl implements FuDocService{
    @Override
    public JavaClassType getJavaClassType() {
        return JavaClassType.OBJECT;
    }


    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {


        return null;
    }
}
