package com.wdf.fudoc.apidoc.parse.object;

import com.intellij.psi.PsiType;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @author wangdingfu
 * @description 对象解析器
 * @date 2022-04-18 20:44:18
 */
public interface ApiDocObjectParser {

    /**
     * 排序规则 按从小到大执行
     *
     * @return 当前解析器的执行顺序
     */
    int sort();

    /**
     * 判断是否满足条件解析
     *
     * @param psiType 需要解析的对象类型
     * @return true 可以解析  false 不支持解析该对象
     */
    boolean isParse(PsiType psiType);


    /**
     * 解析java对象
     *
     * @param psiType       对象类型
     * @param parseObjectBO 解析对象所需要的参数
     * @return 返回解析对象后的一些属性 注解 注释等描述信息
     */
    ObjectInfoDesc parse(PsiType psiType, ParseObjectBO parseObjectBO);
}
