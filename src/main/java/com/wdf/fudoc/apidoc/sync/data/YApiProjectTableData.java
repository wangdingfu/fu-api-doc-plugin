package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.components.bo.TreeTableBO;
import lombok.Getter;
import lombok.Setter;

/**
 * yapi项目配置
 *
 * @author wangdingfu
 * @date 2023-01-07 00:00:11
 */
@Getter
@Setter
public class YApiProjectTableData {

    /**
     * 是否选中
     */
    private Boolean select;

    /**
     * 项目token
     */
    private String projectToken;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 项目范围
     */
    private TreePathBO scope;


}
