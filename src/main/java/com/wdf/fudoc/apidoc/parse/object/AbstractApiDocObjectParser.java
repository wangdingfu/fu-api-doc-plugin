package com.wdf.fudoc.apidoc.parse.object;

import cn.hutool.json.JSONObject;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.wdf.fudoc.apidoc.mock.real.MockRealData;
import com.wdf.fudoc.apidoc.parse.field.FuDocField;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiClass;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiParameter;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocArrayType;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.mock.FuDocObjectJMockData;
import com.wdf.fudoc.apidoc.mock.FuDocObjectMock;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 解析java对象成ApiDoc对象抽象类
 * @date 2022-04-18 20:49:48
 */
public abstract class AbstractApiDocObjectParser implements ApiDocObjectParser {

    private final FuDocObjectMock fuDocObjectMock = new FuDocObjectJMockData();

    /**
     * 获取对象类型
     */
    protected abstract FuDocObjectType getObjectType();

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
        FuDocContext fuDocContext = parseObjectBO.getFuDocContext();
        objectInfoDesc.setDescId(fuDocContext.genDescId());
        FuDocField fuDocField = parseObjectBO.getFuDocField();
        if (Objects.nonNull(fuDocField)) {
            if (fuDocField instanceof FuDocPsiParameter || fuDocField instanceof FuDocPsiClass) {
                //标识根节点
                objectInfoDesc.setRootId(objectInfoDesc.getDescId());
                parseObjectBO.setRootId(objectInfoDesc.getRootId());
                fuDocContext.add(objectInfoDesc.getDescId(), objectInfoDesc);
            }
            objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.PARAM_TYPE, fuDocField.getParamType());
            objectInfoDesc.setDocText(fuDocField.getComment());
            objectInfoDesc.setName(fuDocField.getName());
            objectInfoDesc.setAnnotationDataMap(AnnotationUtils.parse(fuDocField.getAnnotations()));
            addModifierProperty(FuDocConstants.ModifierProperty.STATIC, fuDocField, objectInfoDesc);
            addModifierProperty(FuDocConstants.ModifierProperty.FINAL, fuDocField, objectInfoDesc);
        }
        objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_ATTR, true);
        objectInfoDesc.setTypeView(typeView);
        objectInfoDesc.setType(psiType.getCanonicalText());
        objectInfoDesc.setFuDocObjectType(getObjectType());
        objectInfoDesc.setValue(mockValue(objectInfoDesc, parseObjectBO));
        Integer rootId = parseObjectBO.getRootId();
        if (Objects.nonNull(rootId)) {
            objectInfoDesc.setRootId(rootId);
        }
        return objectInfoDesc;
    }


    private Object mockValue(ObjectInfoDesc objectInfoDesc, ParseObjectBO parseObjectBO) {
        //优先mock有真实请求的数据
        MockRealData mockRealData = parseObjectBO.getMockRealData();
        if (Objects.nonNull(mockRealData)) {
            Object data = mockRealData.getData(objectInfoDesc.getName());
            if (Objects.nonNull(data)) {
                return data;
            }
        }
        return mockCommonType(objectInfoDesc);
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
        if (Objects.isNull(objectInfoDesc) || FuStringUtils.isBlank(type = objectInfoDesc.getType())) {
            return null;
        }
        CommonObjectType commonObjectType = CommonObjectType.getEnum(type);
        Class<?> clazz = null;
        if (commonObjectType.isPrimitiveOrCommon()) {
            clazz = commonObjectType.getClazz();
        }
        if (FuDocObjectType.ARRAY.equals(objectInfoDesc.getFuDocObjectType())) {
            //当前参数为数组
            clazz = FuDocArrayType.getClass(type);
        }
        if (Objects.nonNull(clazz)) {
            return fuDocObjectMock.mock(clazz, objectInfoDesc.getName());
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
                if (objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.STATIC) || objectInfoDesc.getBooleanValue(FuDocConstants.ModifierProperty.FINAL)) {
                    continue;
                }
                jsonObject.putIfAbsent(objectInfoDesc.getName(), objectInfoDesc.getValue());
            }
            return jsonObject;
        }
        return null;
    }


    private void addModifierProperty(String property, FuDocField fuDocField, ObjectInfoDesc objectInfoDesc) {
        if (fuDocField.hasProperty(property)) {
            objectInfoDesc.addExtInfo(property, true);
        }
    }
}
