package com.wdf.fudoc.apidoc.parse.object.impl;

import com.google.common.collect.Lists;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.fudoc.apidoc.factory.ObjectInfoDescFactory;
import com.wdf.fudoc.apidoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.apidoc.parse.field.FuDocCustomerField;
import com.wdf.fudoc.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description map对象解析器
 * @date 2022-04-18 21:33:58
 */
public class FuDocMapParser extends AbstractApiDocObjectParser {
    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.MAP_OBJECT;
    }

    @Override
    public int sort() {
        return 150;
    }

    @Override
    public boolean isParse(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_MAP);
    }

    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        ObjectInfoDesc objectInfoDesc = buildDefault(psiType, "map", parseObjectBO);
        //获取泛型
        Map<String, PsiType> genericsMap = buildGenericsMap(psiType, PsiUtil.resolveClassInType(psiType));
        List<ObjectInfoDesc> childList = Lists.newArrayList();
        childList.add(build(genericsMap.get("K"), parseObjectBO, FuDocConstants.KEY, "属性名(key)", CommonObjectType.STRING.getName()));
        childList.add(build(genericsMap.get("V"), parseObjectBO, FuDocConstants.VALUE, "属性值(value)", CommonObjectType.OBJECT_TYPE.getName()));
        objectInfoDesc.setChildList(childList);
        return objectInfoDesc;
    }


    private ObjectInfoDesc build(PsiType psiType, ParseObjectBO parseObjectBO, String name, String comment, String typeView) {
        if (Objects.isNull(psiType)) {
            return ObjectInfoDescFactory.build(name, typeView, comment);
        }
        ParseObjectBO genericParseObjectBO = new ParseObjectBO(parseObjectBO.getFuDocContext(), parseObjectBO.getMockRealData());
        genericParseObjectBO.setFuDocField(new FuDocCustomerField(name, comment));
        genericParseObjectBO.setRootId(parseObjectBO.getRootId());
        genericParseObjectBO.setParamType(parseObjectBO.getParamType());
        return ObjectParserExecutor.execute(psiType, genericParseObjectBO);
    }

}
