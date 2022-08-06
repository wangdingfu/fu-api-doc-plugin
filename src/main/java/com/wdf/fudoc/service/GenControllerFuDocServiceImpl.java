package com.wdf.fudoc.service;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.FuDocRender;
import com.wdf.fudoc.assemble.AssembleServiceExecutor;
import com.wdf.fudoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.helper.ServiceHelper;
import com.wdf.fudoc.parse.FuDocClassParser;
import com.wdf.fudoc.parse.FuDocClassParserImpl;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.PsiClassUtils;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:48:21
 */
public class GenControllerFuDocServiceImpl implements FuDocService {

    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        //获取当前操作的方法
        PsiMethod targetMethod = PsiClassUtils.getTargetMethod(fuDocContext.getTargetElement());
        //解析java类
        FuDocClassParser fuDocClassParser = ServiceHelper.getService(FuDocClassParserImpl.class);
        ClassInfoDesc classInfoDesc = fuDocClassParser.parse(fuDocContext, psiClass, ObjectUtils.newArrayList(targetMethod));

        //组装ApiDocData对象
        List<FuDocItemData> resultList = AssembleServiceExecutor.execute(fuDocContext, classInfoDesc);

        //将接口文档数据渲染成markdown格式接口文档
        return FuDocRender.markdownRender(fuDocContext.getSettingData(), resultList);
    }
}
