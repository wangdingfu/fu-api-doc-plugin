package com.wdf.apidoc.handler;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 解析java对象成ApiDoc对象抽象类
 * @Date 2022-04-18 20:49:48
 */
public abstract class AbstractParseObjectHandler implements ParseObjectHandler {


    protected ApiDocObjectData buildDefault(PsiType psiType, String type, ParseObjectBO parseObjectBO) {
        ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
        apiDocObjectData.setDocText(parseObjectBO.getDocText());
        apiDocObjectData.setName(parseObjectBO.getName());
        apiDocObjectData.setTypeView(type);
        apiDocObjectData.setType(psiType.getCanonicalText());
        return apiDocObjectData;
    }


    protected Map<String, PsiType> buildGenericsMap(PsiType psiType, PsiClass psiClass) {
        Map<String, PsiType> genericsMap = new HashMap<>();
        if (psiType instanceof PsiClassType) {
            PsiTypeParameter[] typeParameters = psiClass.getTypeParameters();
            if (typeParameters.length > 0) {
                PsiType[] parameters = ((PsiClassType) psiType).getParameters();
                for (int i = 0; i < typeParameters.length; i++) {
                    PsiTypeParameter typeParameter = typeParameters[i];
                    genericsMap.put(typeParameter.getName(), parameters[i]);
                }
            }
        }
        return genericsMap;
    }


    /**
     * 格式化对象类型（如果对象为泛型 则将泛型替换为真实对象类型）
     *
     * @param psiType       对象类型
     * @param parseObjectBO 解析对象所需要的参数
     * @return 实际的对象类型
     */
    protected PsiType formatPsiType(PsiType psiType, ParseObjectBO parseObjectBO) {
        PsiType generics;
        if (Objects.nonNull(parseObjectBO) && Objects.nonNull(generics = parseObjectBO.getPsiType(psiType.getCanonicalText()))) {
            //替换泛型类
            return generics;
        }
        return psiType;
    }
}
