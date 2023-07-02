package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-07-01 15:00:59
 */
@Getter
@Setter
public class ApiFoxProjectTableData implements Serializable {

    /**
     * 是否选中
     */
    private Boolean select = true;

    /**
     * 项目ID-接口文档系统
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目范围
     */
    private TreePathBO scope;

    /**
     * 分类集合
     */
    private List<ApiCategoryDTO> categoryList;
}
