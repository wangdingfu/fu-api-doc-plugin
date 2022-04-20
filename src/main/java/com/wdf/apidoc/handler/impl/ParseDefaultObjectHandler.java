package com.wdf.apidoc.handler.impl;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.bo.PsiClassTypeBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.execute.ParseObjectExecutor;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;
import com.wdf.apidoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 对象解析器(用户自定义对象)
 * @Date 2022-04-18 21:34:42
 */
public class ParseDefaultObjectHandler extends AbstractParseObjectHandler {


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
     * @return 解析后的ApiDoc对象
     */
    @Override
    public ApiDocObjectData parse(PsiType psiType, ParseObjectBO parseObjectBO) {
        List<ApiDocObjectData> apiDocObjectDataList = Lists.newArrayList();
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        parseObject(psiType, psiClass, apiDocObjectDataList);
        ApiDocObjectData apiDocObjectData = buildDefault(psiType, "object", parseObjectBO);
        if (CollectionUtils.isNotEmpty(apiDocObjectDataList)) {
            apiDocObjectData.setChildList(apiDocObjectDataList);
        }
        return apiDocObjectData;
    }


    /**
     * 解析对象 递归遍历父类并解析
     *
     * @param psiType              对象类型
     * @param psiClass             对象class
     * @param apiDocObjectDataList 解析后的对象结果
     */
    private void parseObject(PsiType psiType, PsiClass psiClass, List<ApiDocObjectData> apiDocObjectDataList) {
        if (Objects.nonNull(psiType) && psiType.isValid() && PsiClassUtils.isClass(psiClass)) {
            if (CommonClassNames.JAVA_LANG_OBJECT.equals(psiClass.getQualifiedName())) {
                //当前class的父类是java.lang.Object时 则直接返回
                return;
            }
            //解析指定对象为ApiDoc文档对象
            apiDocObjectDataList.addAll(doParseObject(psiType, psiClass));
            //获取父类
            PsiClassTypeBO superClassType = PsiClassUtils.getSuperClassType(psiClass);
            if (Objects.nonNull(superClassType)) {
                //继续遍历解析父类
                parseObject(superClassType.getPsiType(), superClassType.getPsiClass(), apiDocObjectDataList);
            }
        }
    }


    /**
     * 解析对象
     *
     * @param psiType  对象类型
     * @param psiClass 对象class
     * @return 解析对象后的ApiDoc对象
     */
    private List<ApiDocObjectData> doParseObject(PsiType psiType, PsiClass psiClass) {
        List<ApiDocObjectData> childList = Lists.newArrayList();
        PsiField[] fields = psiClass.getFields();
        if (fields.length > 0) {
            ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
            ParseObjectBO parentBO = new ParseObjectBO();
            parentBO.setGenericsMap(buildGenericsMap(psiType, psiClass));
            for (PsiField psiField : psiClass.getFields()) {
                parentBO.setPsiField(psiField);
                childList.add(ParseObjectExecutor.execute(psiField.getType(), parentBO));
            }
            childList.removeAll(Collections.singleton(null));
            apiDocObjectData.setChildList(childList);
        }
        return childList;
    }
}
