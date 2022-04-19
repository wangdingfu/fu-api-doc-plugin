package com.wdf.apidoc.bo;

import com.wdf.apidoc.enumtype.PsiObjectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 对象类型BO(用于描述一个对象的类型)
 * @date 2022-04-10 21:23:18
 */
@Getter
@Setter
public class ApiDocObjectTypeBO {

    /**
     * 对外显示的字段类型
     */
    private String typeView;


    /**
     * 当前类型组类别
     */
    private PsiObjectType objectType;

    /**
     * 字段类型全称
     */
    private String qualifiedName;


    /**
     * 是否为公共对象(jdk自带 || 第三方框架对象(例如spring框架等))
     */
    private boolean commonObject;


    /**
     * 当前对象子节点或则泛型对象结构
     */
    private List<ApiDocObjectTypeBO> childList;

}

