package com.wdf.fudoc.apidoc.service;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.apidoc.assemble.AssembleServiceExecutor;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.apidoc.parse.FuDocClassParser;
import com.wdf.fudoc.apidoc.parse.FuDocClassParserImpl;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.util.GenFuDocUtils;
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
        //组装ApiDocData对象
        List<FuDocItemData> resultList = GenFuDocUtils.gen(fuDocContext, psiClass);

        //将接口文档数据渲染成markdown格式接口文档
        return FuDocRender.markdownRender(fuDocContext.getSettingData(), resultList);
    }
}
