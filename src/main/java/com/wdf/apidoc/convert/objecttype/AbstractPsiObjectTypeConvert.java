package com.wdf.apidoc.convert.objecttype;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;
import com.wdf.apidoc.enumtype.PsiObjectType;

/**
 * @author wangdingfu
 * @descption: 对象转换抽象类
 * @date 2022-04-16 20:18:47
 */
public abstract class AbstractPsiObjectTypeConvert implements PsiObjectTypeConvert {


    /**
     * 构建一个基本的对象类型描述信息
     *
     * @param psiType  对象类型
     * @param type     具体描述对象的类型({@link PsiObjectType psiObjectType})
     * @param typeView 对外显示的类型
     * @return 一个对象类型的描述信息
     */
    protected ApiDocObjectTypeBO build(PsiType psiType, PsiObjectType type, String typeView) {
        ApiDocObjectTypeBO apiDocObjectTypeBO = new ApiDocObjectTypeBO();
        String canonicalText = psiType.getCanonicalText();
        apiDocObjectTypeBO.setQualifiedName(canonicalText);
        apiDocObjectTypeBO.setCommonObject(true);
        apiDocObjectTypeBO.setObjectType(type);
        return apiDocObjectTypeBO;
    }
}
