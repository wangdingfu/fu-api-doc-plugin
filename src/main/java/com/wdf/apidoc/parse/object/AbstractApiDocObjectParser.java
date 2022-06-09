package com.wdf.apidoc.parse.object;

import com.alibaba.fastjson.JSONObject;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.wdf.apidoc.constant.FuDocConstants;
import com.wdf.apidoc.constant.enumtype.ApiDocArrayType;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.apidoc.mock.ApiDocObjectJMockData;
import com.wdf.apidoc.mock.ApiDocObjectMock;
import com.wdf.apidoc.parse.field.ApiDocField;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.AnnotationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析java对象成ApiDoc对象抽象类
 * @Date 2022-04-18 20:49:48
 */
public abstract class AbstractApiDocObjectParser implements ApiDocObjectParser {

    private final ApiDocObjectMock apiDocObjectMock = new ApiDocObjectJMockData();

    /**
     * 获取对象类型
     */
    protected abstract ApiDocObjectType getObjectType();

    /**
     * 构建一个默认的ApiDoc对象
     *
     * @param psiType       对象类型
     * @param typeView      页面文档显示的类型
     * @param parseObjectBO 解析对象参数
     * @return 指定对象解析后的接口文档描述信息
     */
    protected ObjectInfoDesc buildDefault(PsiType psiType, String typeView, ParseObjectBO parseObjectBO) {
        ObjectInfoDesc objectInfoDesc = new ObjectInfoDesc();
        ApiDocField apiDocField = parseObjectBO.getApiDocField();
        if (Objects.nonNull(apiDocField)) {
            objectInfoDesc.setDocText(apiDocField.getComment());
            objectInfoDesc.setName(apiDocField.getName());
            objectInfoDesc.setAnnotationDataMap(AnnotationUtils.parse(apiDocField.getAnnotations()));
            addModifierProperty(FuDocConstants.ModifierProperty.STATIC, apiDocField, objectInfoDesc);
            addModifierProperty(FuDocConstants.ModifierProperty.FINAL, apiDocField, objectInfoDesc);
        }
        objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_ATTR, true);
        objectInfoDesc.setTypeView(typeView);
        objectInfoDesc.setType(psiType.getCanonicalText());
        objectInfoDesc.setApiDocObjectType(getObjectType());
        objectInfoDesc.setValue(mockCommonType(objectInfoDesc));
        return objectInfoDesc;
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


    /**
     * mock常用的基本数据类型(不递归遍历 只mock当前层级的参数)
     *
     * @param objectInfoDesc 对象信息描述
     * @return mock后的数据
     */
    protected Object mockCommonType(ObjectInfoDesc objectInfoDesc) {
        String type;
        if (Objects.isNull(objectInfoDesc) || StringUtils.isBlank(type = objectInfoDesc.getType())) {
            return null;
        }
        CommonObjectType commonObjectType = CommonObjectType.getEnum(type);
        Class<?> clazz = null;
        if (commonObjectType.isPrimitiveOrCommon()) {
            clazz = commonObjectType.getClazz();
        }
        if (ApiDocObjectType.ARRAY.equals(objectInfoDesc.getApiDocObjectType())) {
            //当前参数为数组
            clazz = ApiDocArrayType.getClass(type);
        }
        if (Objects.nonNull(clazz)) {
            return apiDocObjectMock.mock(clazz, objectInfoDesc.getName());
        }
        return null;
    }

    /**
     * 根据子属性集合的值组装父属性的值
     *
     * @param childList 子属性集合
     * @return 父属性的值
     */
    protected JSONObject buildValue(List<ObjectInfoDesc> childList) {
        if (CollectionUtils.isNotEmpty(childList)) {
            JSONObject jsonObject = new JSONObject();
            for (ObjectInfoDesc objectInfoDesc : childList) {
                if (objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.STATIC)
                        || objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.FINAL)) {
                    continue;
                }
                jsonObject.put(objectInfoDesc.getName(), objectInfoDesc.getValue());
            }
            return jsonObject;
        }
        return null;
    }


    private void addModifierProperty(String property, ApiDocField apiDocField, ObjectInfoDesc objectInfoDesc) {
        if (apiDocField.hasProperty(property)) {
            objectInfoDesc.addExtInfo(property, true);
        }
    }
}
