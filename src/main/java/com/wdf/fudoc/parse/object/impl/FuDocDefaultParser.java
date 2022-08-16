package com.wdf.fudoc.parse.object.impl;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.constant.CommonObjectNames;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.FuDocObjectType;
import com.wdf.fudoc.constant.enumtype.ParamType;
import com.wdf.fudoc.parse.ObjectParserExecutor;
import com.wdf.fudoc.parse.field.FuDocField;
import com.wdf.fudoc.parse.field.FuDocPsiField;
import com.wdf.fudoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 默认对象解析器(用户自定义对象)
 * @date 2022-04-18 21:34:42
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
        FuDocContext fuDocContext = parseObjectBO.getFuDocContext();
        String canonicalText = psiType.getCanonicalText();
        ObjectInfoDesc objectInfoDesc = buildDefaultObjectInfoDesc(psiType, parseObjectBO);
        ObjectInfoDesc objectInfoDescCache = fuDocContext.getFromCache(canonicalText);
        if (Objects.isNull(objectInfoDescCache)) {
            //缓存没有 需要解析
            PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
            parseObjectBO.setGenericsMap(buildGenericsMap(psiType, psiClass));
            //添加到EarlyMap中（半成品对象）
            fuDocContext.add(canonicalText, objectInfoDesc);
            //解析对象
            paddingChildList(objectInfoDesc, doParseDefaultObject(parseObjectBO, psiType, psiClass));
            //当前对象解析完成 从earlyMap中移动到objectInfoDescMap中（从半成品变为成品）
            fuDocContext.parseFinish(canonicalText);
        } else {
            //将缓存中之前解析的设置到当前对象中 直接返回 避免重复解析(此处直接返回也是为了避免循环引用)
            List<ObjectInfoDesc> childList = ObjectUtils.listToList(objectInfoDescCache.getChildList(), data -> BeanUtil.copyProperties(data, ObjectInfoDesc.class));
            paddingRootId(objectInfoDesc.getRootId(), childList);
            paddingChildList(objectInfoDesc, childList);
        }
        return objectInfoDesc;
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
        if (Objects.nonNull(psiType) && psiType.isValid() && PsiClassUtils.isClass(psiClass) && !CommonClassNames.JAVA_LANG_OBJECT.equals(psiClass.getQualifiedName())) {
            FuDocContext fuDocContext = parseObjectBO.getFuDocContext();
            Map<String, String> filterMap = fuDocContext.getFilterMap();
            String fields = filterMap.get(psiClass.getQualifiedName());
            if (Objects.nonNull(fields) && StringUtils.EMPTY.equals(fields)) {
                //没有配置属性 则代表不解析当前类
                return childList;
            }
            List<String> fieldNameList = Lists.newArrayList(Objects.isNull(fields) ? new String[]{} : StringUtils.split(fields, ","));
            //遍历当前类的所有字段（包含父类）
            for (PsiField psiField : psiClass.getAllFields()) {
                if (fieldNameList.contains(psiField.getName()) || fieldIsIgnore(psiField, parseObjectBO)) {
                    //如果属性在过滤列表里 则标识该属性呗过滤掉了
                    continue;
                }
                parseObjectBO.setFuDocField(new FuDocPsiField(psiField));
                childList.add(ObjectParserExecutor.execute(psiField.getType(), parseObjectBO));
            }
            childList.removeAll(Collections.singleton(null));
        }
        return childList;
    }


    /**
     * 判断当前字段是否标识Ignore注解被忽略了
     * <p>
     * 1、被"@JsonIgnore"注解标识了会被忽略
     * 2、被“@JsonProperty”注解标识
     * 2.1、access属性的值为READ_ONLY 则只会解析请求参数
     * 2.2、access属性的值为WRITE_ONLY 则只会解析响应参数
     * 2.3、其他情况都会解析
     *
     * @param psiField      字段
     * @param parseObjectBO 解析字段的参数
     * @return true: 需要忽略该字段
     */
    private boolean fieldIsIgnore(PsiField psiField, ParseObjectBO parseObjectBO) {
        PsiAnnotation annotation = psiField.getAnnotation(CommonObjectNames.JSON_IGNORE);
        if (Objects.nonNull(annotation)) {
            //被JsonIgnore注解标识 需要忽略改属性
            return true;
        }
        AnnotationData annotationData = AnnotationUtils.parse(psiField.getAnnotation(CommonObjectNames.JSON_PROPERTY));
        String enumValue;
        if (Objects.nonNull(annotationData) && StringUtils.isNotBlank(enumValue = annotationData.enumValue("access").getEnumValue())) {
            ParamType paramType = parseObjectBO.getParamType();
            if (ParamType.REQUEST_PARAM.equals(paramType) && "WRITE_ONLY".equals(enumValue)) {
                //请求参数 标识了只写 说明不读取该参数 则返回忽略
                return true;
            }
            //响应参数 标识了只读 说明不会将该参数返回出去 则返回忽略
            return ParamType.RESPONSE_PARAM.equals(paramType) && "READ_ONLY".equals(enumValue);
        }
        return false;
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


    private void paddingRootId(Integer rootId, List<ObjectInfoDesc> childList) {
        if (CollectionUtils.isNotEmpty(childList)) {
            for (ObjectInfoDesc objectInfoDesc : childList) {
                objectInfoDesc.setRootId(rootId);
                List<ObjectInfoDesc> children = objectInfoDesc.getChildList();
                if (CollectionUtils.isNotEmpty(children)) {
                    paddingRootId(rootId, children);
                }
            }
        }
    }
}
