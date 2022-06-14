package com.wdf.apidoc.parse.object.impl;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.constant.FuDocConstants;
import com.wdf.apidoc.constant.enumtype.FuDocObjectType;
import com.wdf.apidoc.parse.ObjectParserExecutor;
import com.wdf.apidoc.parse.field.FuDocField;
import com.wdf.apidoc.parse.field.FuDocPsiField;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 默认对象解析器(用户自定义对象)
 * @Date 2022-04-18 21:34:42
 */
public class FuDocDefaultParser extends AbstractApiDocObjectParser {

    @Override
    protected FuDocObjectType getObjectType() {
        return FuDocObjectType.DEFAULT_OBJECT;
    }

    /**
     * 默认最后解析  只有所有的解析器都无法解析时 才会走当前的解析器
     *
     * @return 返回最大值 即将当前解析器排序在最后一位
     */
    @Override
    public int sort() {
        return Integer.MAX_VALUE;
    }

    /**
     * 永远返回true 如果前面所有的解析器都不能解析 则当前解析器负责解析
     *
     * @param psiType 需要解析的对象类型
     * @return 默认解析所有对象
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return psiType instanceof PsiClassType;
    }


    /**
     * 解析对象（一般为用户自定义对象）
     * 只有当其他所有解析器无法解析时 才会走当前解析器来解析
     *
     * @param psiType       对象类型
     * @param parseObjectBO 解析对象的参数
     * @return 返回解析对象后的一些属性 注解 注释等描述信息
     */
    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        ApiDocContext apiDocContext = parseObjectBO.getApiDocContext();
        String canonicalText = psiType.getCanonicalText();
        ObjectInfoDesc objectInfoDesc = buildDefaultObjectInfoDesc(psiType, parseObjectBO);
        ObjectInfoDesc objectInfoDescCache = apiDocContext.getFromCache(canonicalText);
        if (Objects.isNull(objectInfoDescCache)) {
            //缓存没有 需要解析
            PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
            parseObjectBO.setGenericsMap(buildGenericsMap(psiType, psiClass));
            //添加到EarlyMap中（半成品对象）
            apiDocContext.add(canonicalText, objectInfoDesc);
            //解析对象
            paddingChildList(objectInfoDesc, doParseDefaultObject(parseObjectBO, psiType, psiClass));
            //当前对象解析完成 从earlyMap中移动到objectInfoDescMap中（从半成品变为成品）
            apiDocContext.parseFinish(canonicalText);
        } else {
            //将缓存中之前解析的设置到当前对象中 直接返回 避免重复解析(此处直接返回也是为了避免循环引用)
            paddingChildList(objectInfoDesc, objectInfoDescCache.getChildList());
        }
        return objectInfoDesc;
    }

    private ObjectInfoDesc buildDefaultObjectInfoDesc(PsiType psiType, ParseObjectBO parseObjectBO) {
        ObjectInfoDesc objectInfoDesc = buildDefault(psiType, "object", parseObjectBO);
        FuDocField fuDocField = parseObjectBO.getFuDocField();
        boolean isAttr = Objects.nonNull(fuDocField) && fuDocField instanceof FuDocPsiField;
        objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.IS_ATTR, isAttr);
        return objectInfoDesc;
    }


    private void paddingChildList(ObjectInfoDesc objectInfoDesc, List<ObjectInfoDesc> childList) {
        if (CollectionUtils.isNotEmpty(childList)) {
            objectInfoDesc.setChildList(childList);
            objectInfoDesc.setValue(buildValue(childList));
        }
    }

    /**
     * 解析对象 递归遍历父类并解析
     *
     * @param parseObjectBO 解析对象参数
     * @param psiType       对象类型
     * @param psiClass      对象class
     */
    private List<ObjectInfoDesc> doParseDefaultObject(ParseObjectBO parseObjectBO, PsiType psiType, PsiClass psiClass) {
        List<ObjectInfoDesc> childList = Lists.newArrayList();
        if (Objects.nonNull(psiType) && psiType.isValid()
                && PsiClassUtils.isClass(psiClass)
                && !CommonClassNames.JAVA_LANG_OBJECT.equals(psiClass.getQualifiedName())) {
            //遍历当前类的所有字段（包含父类）
            for (PsiField psiField : psiClass.getAllFields()) {
                parseObjectBO.setFuDocField(new FuDocPsiField(psiField));
                childList.add(ObjectParserExecutor.execute(psiField.getType(), parseObjectBO));
            }
            childList.removeAll(Collections.singleton(null));
        }
        return childList;
    }

}