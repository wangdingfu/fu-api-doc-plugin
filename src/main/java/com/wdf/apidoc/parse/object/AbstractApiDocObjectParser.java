package com.wdf.apidoc.parse.object;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.wdf.apidoc.helper.AnnotationParseHelper;
import com.wdf.apidoc.parse.field.ApiDocField;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析java对象成ApiDoc对象抽象类
 * @Date 2022-04-18 20:49:48
 */
public abstract class AbstractApiDocObjectParser implements ApiDocObjectParser {


    /**
     * 构建一个默认的ApiDoc对象
     *
     * @param psiType       对象类型
     * @param typeView      页面文档显示的类型
     * @param parseObjectBO 解析对象参数
     * @return 指定对象解析后的接口文档描述信息
     */
    protected ApiDocObjectData buildDefault(PsiType psiType, String typeView, ParseObjectBO parseObjectBO) {
        ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
        ApiDocField apiDocField = parseObjectBO.getApiDocField();
        if (Objects.nonNull(apiDocField)) {
            apiDocObjectData.setDocText(apiDocField.getComment());
            apiDocObjectData.setName(apiDocField.getName());
            apiDocObjectData.setAnnotationDataMap(AnnotationParseHelper.parse(apiDocField.getAnnotations()));
        }
        apiDocObjectData.setTypeView(typeView);
        apiDocObjectData.setType(psiType.getCanonicalText());
        apiDocObjectData.setFilterObject(false);
        return apiDocObjectData;
    }


    /**
     * 获取类上的泛型和字段泛型的对应关系
     *
     * @param psiType  对象类型
     * @param psiClass 对象class
     * @return key:泛型（例如T,E） value:泛型对应的对象类型
     */
    protected Map<String, PsiType> buildGenericsMap(PsiType psiType, PsiClass psiClass) {
        Map<String, PsiType> genericsMap = new HashMap<>();
        if (psiType instanceof PsiClassType) {
            PsiTypeParameter[] typeParameters = psiClass.getTypeParameters();
            PsiType[] parameters = ((PsiClassType) psiType).getParameters();
            int parametersLength = parameters.length;
            if (typeParameters.length > 0 && parametersLength > 0) {
                for (int i = 0; i < typeParameters.length; i++) {
                    PsiTypeParameter typeParameter = typeParameters[i];
                    if (parametersLength > i) {
                        //防止数组越界 （有可能某些类没有指定泛型的具体类）
                        genericsMap.put(typeParameter.getName(), parameters[i]);
                    }
                }
            }
        }
        return genericsMap;
    }


}
