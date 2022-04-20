package com.wdf.apidoc.handler;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.data.ApiDocObjectData;

/**
 * @author wangdingfu
 * @Descption 解析对象
 * @Date 2022-04-18 20:44:18
 */
public interface ParseObjectHandler {

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
     * 解析java对象成ApiDoc对象
     *
     * @param psiType 对象类型
     * @param parent  对象所属父级对象的信息bo
     * @return 解析后的ApiDoc对象
     */
    ApiDocObjectData parse(PsiType psiType, ParseObjectBO parent);
}
