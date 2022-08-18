package com.wdf.fudoc.service;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.wdf.fudoc.FuDocRender;
import com.wdf.fudoc.constant.enumtype.ParamType;
import com.wdf.fudoc.helper.AssembleHelper;
import com.wdf.fudoc.helper.MockDataHelper;
import com.wdf.fudoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;

import java.util.List;

/**
 * 将java bean 生成json对象
 * @author wangdingfu
 * @date 2022-07-18 20:52:37
 */
public class GenObjectJsonServiceImpl implements FuDocService {


    @Override
    public String genFuDocContent(FuDocContext fuDocContext, PsiClass psiClass) {
        ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext);
        List<ObjectInfoDesc> fieldList = Lists.newArrayList();
        //解析对象参数
        for (PsiField psiField : psiClass.getAllFields()) {
            parseObjectBO.setFuDocField(new FuDocPsiField(psiField));
            parseObjectBO.setParamType(ParamType.NONE);
            fieldList.add(ObjectParserExecutor.execute(psiField.getType(), parseObjectBO));
        }
        return MockDataHelper.mockJsonData(fieldList);
    }
}
