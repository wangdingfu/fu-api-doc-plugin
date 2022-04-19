package com.wdf.apidoc.handler.impl;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.bo.ParseObjectParentBO;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.enumtype.CommonObjectType;
import com.wdf.apidoc.handler.AbstractParseObjectHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption 集合对象解析器
 * @Date 2022-04-18 21:33:19
 */
public class ParseCollectionObjectHandler extends AbstractParseObjectHandler {


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
    public ApiDocObjectData parse(PsiType psiType, ParseObjectParentBO parent) {
        ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
        //获取集合的泛型对象
        PsiType iterableType = PsiUtil.extractIterableTypeParameter(psiType, false);
        if (Objects.isNull(iterableType)) {
            //没有泛型 无法继续遍历 直接退出解析
            return apiDocObjectData;
        }
        //基本数据类型和公共数据类型 则不处理
        if (iterableType instanceof PsiPrimitiveType || StringUtils.isNotBlank(CommonObjectType.getName(psiType.getCanonicalText()))) {
            apiDocObjectData.setCanonicalText(psiType.getPresentableText());
            return apiDocObjectData;
        }

        //泛型填充

        //遍历泛型对象


        return null;
    }


}
