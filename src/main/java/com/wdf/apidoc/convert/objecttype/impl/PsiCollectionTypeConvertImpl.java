package com.wdf.apidoc.convert.objecttype.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.convert.objecttype.AbstractPsiObjectTypeConvert;
import com.wdf.apidoc.enumtype.CommonObjectType;
import com.wdf.apidoc.enumtype.PsiObjectType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 集合类型转换
 * @date 2022-04-16 20:39:58
 */
public class PsiCollectionTypeConvertImpl extends AbstractPsiObjectTypeConvert {

    @Override
    public int sort() {
        return 100;
    }

    /**
     * 判断是否为集合类型
     *
     * @param psiType 对象类型
     * @return 是否需要解析该类型
     */
    @Override
    public boolean isMatch(PsiType psiType) {
        return InheritanceUtil.isInheritor(psiType, CommonClassNames.JAVA_UTIL_COLLECTION);
    }


    /**
     * 解析集合类型
     *
     * @param psiType 对象类型
     * @return 集合类型解析后的描述对象
     */
    @Override
    public ApiDocObjectTypeBO parse(PsiType psiType) {
        ApiDocObjectTypeBO apiDocObjectTypeBO = buildList(psiType);
        //获取集合的泛型对象
        PsiType iterableType = PsiUtil.extractIterableTypeParameter(psiType, false);
        if (Objects.isNull(iterableType)) {
            //没有泛型 无法继续遍历 直接退出解析
            return apiDocObjectTypeBO;
        }
        //基本数据类型和公共数据类型 则不处理
        if (iterableType instanceof PsiPrimitiveType || StringUtils.isNotBlank(CommonObjectType.getName(psiType.getCanonicalText()))) {
            apiDocObjectTypeBO.setTypeView(psiType.getPresentableText());
            return apiDocObjectTypeBO;
        }

        //泛型填充

        //遍历泛型对象


        return null;
    }


    private ApiDocObjectTypeBO buildList(PsiType psiType) {
        return build(psiType, PsiObjectType.COLLECTION, "array");
    }
}
