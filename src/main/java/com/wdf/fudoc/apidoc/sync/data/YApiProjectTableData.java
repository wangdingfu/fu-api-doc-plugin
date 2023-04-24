package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * yapi项目配置
 *
 * @author wangdingfu
 * @date 2023-01-07 00:00:11
 */
@Getter
@Setter
public class YApiProjectTableData implements Serializable {

    /**
     * 是否选中
     */
    private Boolean select;

    /**
     * 项目token-接口文档系统
     */
    private String projectToken;

    /**
     * 项目ID-接口文档系统
     */
    private String projectId;

    /**
     * 项目名称-接口文档系统
     */
    private String projectName;

    /**
     * 项目标识-ide中打开的项目代码
     * 可能会有多个项目关联接口文档的项目
     */
    private List<String> projectKeyList = Lists.newArrayList();


    /**
     * 项目范围
     */
    private TreePathBO scope;


}
