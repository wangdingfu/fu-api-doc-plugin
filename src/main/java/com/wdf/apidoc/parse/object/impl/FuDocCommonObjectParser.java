package com.wdf.apidoc.parse.object.impl;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.constant.enumtype.ApiDocObjectType;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.constant.enumtype.CommonObjectType;
import com.wdf.apidoc.parse.object.AbstractApiDocObjectParser;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @description java 公共常用对象解析器
 * @Date 2022-04-18 21:26:53
 */
public class FuDocCommonObjectParser extends AbstractApiDocObjectParser {

    /**
     * 公共对象
     */
    @Override
    protected ApiDocObjectType getObjectType() {
        return ApiDocObjectType.COMMON_OBJECT;
    }

    /**
     * 执行顺序
     *
     * @return 第二个执行
     */
    @Override
    public int sort() {
        return 1;
    }

    /**
     * 判断是否为java的公共对象 即包装类或者常用类
     *
     * @param psiType 需要解析的对象类型
     * @return 返回true则需要调用parse方法解析该对象
     */
    @Override
    public boolean isParse(PsiType psiType) {
        return CommonObjectType.isCommon(psiType.getCanonicalText());
    }


    /**
     * 解析java公共对象成ApiDoc文档数据对象
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return 返回解析对象后的一些属性 注解 注释等描述信息
     */
    @Override
    public ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parent) {
        return buildDefault(psiType, CommonObjectType.getName(psiType.getCanonicalText()), parent);
    }
}
