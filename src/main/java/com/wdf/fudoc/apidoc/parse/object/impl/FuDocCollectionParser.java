package com.wdf.fudoc.apidoc.parse.object.impl;

import com.google.common.collect.Lists;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.apidoc.mock.real.JsonRealDataHandler;
import com.wdf.fudoc.apidoc.mock.real.MockRealData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.fudoc.apidoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 集合解析器
 * @date 2022-04-18 21:33:19
 */
public class FuDocCollectionParser extends AbstractApiDocObjectParser {


    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.COLLECTION_OBJECT;
    }


    /**
     * 执行顺序 在默认解析器前执行
     *
     * @return 返回值需要大于默认对象解析器 即要排在默认对象解析器前解析
     */
    @Override
    public int sort() {
        return 100;
    }

    /**
     * 判断是否为集合类型
     *
     * @param psiType 对象类型
     * @return true标识该类型为集合类型 可以解析该类型
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_COLLECTION);
    }

    /**
     * 解析集合类型
     *
     * @param psiType 对象类型
     * @return 集合类型解析后的描述对象
     */
    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        ObjectInfoDesc objectInfoDesc = buildDefault(psiType, getCollectionType(null), parseObjectBO);
        //获取集合的泛型对象
        PsiType genericsType = PsiUtil.extractIterableTypeParameter(psiType, false);
        if (Objects.isNull(genericsType)) {
            //没有泛型 无法继续遍历 直接退出解析
            return objectInfoDesc;
        }
        String canonicalText = genericsType.getCanonicalText();
        CommonObjectType commonObjectType = CommonObjectType.getEnum(canonicalText);
        ObjectInfoDesc genericsInfoDesc;
        ParseObjectBO genericsParseObjectBO = new ParseObjectBO(parseObjectBO.getFuDocContext(), parseObjectBO.getMockRealData());
        if (commonObjectType.isPrimitiveOrCommon()) {
            //基本数据类型和公共数据类型 可以直接解析返回
            genericsInfoDesc = buildDefault(genericsType, commonObjectType.getName(), genericsParseObjectBO);
            objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.GENERICS_TYPE, commonObjectType.getFuDocObjectType());
            objectInfoDesc.setChildList(Lists.newArrayList(genericsInfoDesc));
            objectInfoDesc.setChildTypeView(commonObjectType.getName());
        } else {
            //非基本数据类型和常用对象类型 需要深度解析
            genericsParseObjectBO.setGenericsMap(parseObjectBO.getGenericsMap());
            genericsParseObjectBO.setRootId(parseObjectBO.getRootId());
            genericsParseObjectBO.setParamType(parseObjectBO.getParamType());
            MockRealData mockRealData = genericsParseObjectBO.getMockRealData();
            genericsParseObjectBO.setMockRealData(new JsonRealDataHandler(Objects.nonNull(mockRealData) ? mockRealData.getData(null) : null));
            genericsInfoDesc = ObjectParserExecutor.execute(genericsType, genericsParseObjectBO);
            if (Objects.nonNull(genericsInfoDesc)) {
                //将泛型对象的字段集合设置到当前apiDoc中
                FuDocObjectType fuDocObjectType = genericsInfoDesc.getFuDocObjectType();
                List<ObjectInfoDesc> childList = genericsInfoDesc.getChildList();
                if (genericsInfoDesc.getBooleanValue(FuDocConstants.ExtInfo.IS_ATTR)) {
                    childList = Lists.newArrayList(genericsInfoDesc);
                }
                objectInfoDesc.setChildList(childList);
                objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.GENERICS_TYPE, fuDocObjectType);
                objectInfoDesc.setChildTypeView(genericsInfoDesc.getTypeView());
            }
        }
        Object value;
        if (Objects.nonNull(genericsInfoDesc) && Objects.nonNull(value = genericsInfoDesc.getValue())) {
            objectInfoDesc.setValue(Lists.newArrayList(value));
        }
        return objectInfoDesc;
    }


    private String getCollectionType(String name) {
        if (FuStringUtils.isBlank(name)) {
            return "array";
        }
        return String.format("array[%s]", name);
    }


}
