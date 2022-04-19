package com.wdf.apidoc.convert.paramter;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiParameter;
import com.wdf.apidoc.context.ApiDocContext;
import com.wdf.apidoc.data.ApiDocObjectData;

import java.util.Objects;

/**
 * @descption: 参数转换接口实现类
 * @author wangdingfu
 * @date 2022-04-10 20:45:55
 */
public class PsiParameterConvertImpl implements PsiParameterConvert {


    @Override
    public ApiDocObjectData convert(ApiDocContext apiDocContext, PsiParameter psiParameter, String docText) {
        Project project;
        if (Objects.isNull(apiDocContext) || Objects.isNull(project = apiDocContext.getProject()) || Objects.isNull(psiParameter)) {
            return null;
        }
        ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
        return null;
    }


}
