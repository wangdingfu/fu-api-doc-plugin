package com.wdf.fudoc.apidoc.service;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamType;
import com.wdf.fudoc.apidoc.helper.AssembleHelper;
import com.wdf.fudoc.apidoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:52:37
 */
public class GenObjectFuDocServiceImpl implements FuDocService {


    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext, null);
        List<ObjectInfoDesc> fieldList = Lists.newArrayList();
        //解析对象参数
        for (PsiField psiField : psiClass.getAllFields()) {
            parseObjectBO.setFuDocField(new FuDocPsiField(psiField));
            parseObjectBO.setParamType(ParamType.NONE);
            fieldList.add(ObjectParserExecutor.execute(psiField.getType(), parseObjectBO));
        }

        //组装对象参数
        List<FuDocParamData> fuDocParamData = AssembleHelper.assembleParamData(fuDocContext, fieldList, null);

        //将接口文档数据渲染成markdown格式接口文档
        return FuDocRender.paramRender(fuDocParamData, fuDocContext.getSettingData());
    }
}
