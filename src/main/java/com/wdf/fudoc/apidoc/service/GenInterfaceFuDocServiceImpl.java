package com.wdf.fudoc.apidoc.service;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.util.GenFuDocUtils;

import java.util.List;

/**
 * 将java接口生成接口文档
 *
 * @author wangdingfu
 * @date 2022-07-20 16:59:34
 */
public class GenInterfaceFuDocServiceImpl implements FuDocService {

    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        //组装ApiDocData对象
        List<FuDocItemData> resultList = GenFuDocUtils.gen(fuDocContext, psiClass);

        //将接口文档数据渲染成markdown格式接口文档
        return FuDocRender.markdownRender(fuDocContext.getSettingData(), resultList);
    }
}
