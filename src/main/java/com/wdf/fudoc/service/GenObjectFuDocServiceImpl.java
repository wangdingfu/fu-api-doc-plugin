package com.wdf.fudoc.service;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.wdf.fudoc.FuDocRender;
import com.wdf.fudoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.helper.AssembleHelper;
import com.wdf.fudoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:52:37
 */
public class GenObjectFuDocServiceImpl implements FuDocService {


    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext);
        List<ObjectInfoDesc> fieldList = Lists.newArrayList();
        //解析对象参数
        for (PsiField psiField : psiClass.getAllFields()) {
            parseObjectBO.setFuDocField(new FuDocPsiField(psiField));
            fieldList.add(ObjectParserExecutor.execute(psiField.getType(), parseObjectBO));
        }

        //组装对象参数
        List<FuDocParamData> fuDocParamData = AssembleHelper.assembleParamData(fuDocContext, fieldList, null);

        //将接口文档数据渲染成markdown格式接口文档
        return FuDocRender.paramRender(fuDocParamData);
    }
}
