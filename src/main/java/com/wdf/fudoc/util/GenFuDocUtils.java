package com.wdf.fudoc.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.assemble.AssembleServiceExecutor;
import com.wdf.fudoc.apidoc.parse.FuDocClassParser;
import com.wdf.fudoc.apidoc.parse.FuDocClassParserImpl;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.common.ServiceHelper;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-09-18 17:53:46
 */
public class GenFuDocUtils {


    public static List<FuDocItemData> gen(FuDocContext fuDocContext, PsiClass psiClass) {
        //获取当前操作的方法
        PsiMethod targetMethod = PsiClassUtils.getTargetMethod(fuDocContext.getTargetElement());
        //解析java类
        FuDocClassParser fuDocClassParser = ServiceHelper.getService(FuDocClassParserImpl.class);
        ClassInfoDesc classInfoDesc = fuDocClassParser.parse(fuDocContext, psiClass, ObjectUtils.newArrayList(targetMethod));

        //组装ApiDocData对象
        return AssembleServiceExecutor.execute(fuDocContext, classInfoDesc);
    }
}
