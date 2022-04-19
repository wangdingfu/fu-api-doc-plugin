package com.wdf.apidoc.convert.objecttype;

import com.intellij.psi.PsiType;
import com.wdf.apidoc.bo.ApiDocObjectTypeBO;

/**
 * @author wangdingfu
 * @descption: 对象转换接口(不是基础数据类型)
 * @date 2022-04-16 20:03:03
 */
public interface PsiObjectTypeConvert {

    /**
     * 排序规则(根据转换器的顺序从小到大转换)
     */
    int sort();

    /**
     * 实现类判断当前是否符合匹配 如果匹配成功则会调用解析方法解析该类型
     * @param psiType 对象类型
     * @return true: 代表匹配成功 会走实现类解析逻辑  false:代表匹配失败 不会走该实现类解析逻辑
     */
    boolean isMatch(PsiType psiType);


    /**
     * 根据对象类型解析对象
     * @param psiType 对象类型
     * @return 返回描述该对象的整体结构对象
     */
    ApiDocObjectTypeBO parse(PsiType psiType);


}
