package com.wdf.fudoc.pojo.data;

import com.intellij.psi.PsiParameter;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author wangdingfu
 * @Description 根节点参数数据对象
 * @Date 2022-06-26 21:33:33
 */
@Getter
@Setter
public class ParamRootData {

    /**
     * 参数集合
     * key:参数唯一标识(包路径) value:参数对象
     */
    private Map<String, PsiParameter> psiParameterMap;



}
