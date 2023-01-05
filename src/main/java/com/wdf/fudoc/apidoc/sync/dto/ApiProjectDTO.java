package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 接口文档项目对象（一个项目会有多个分类）
 *
 * @author wangdingfu
 * @date 2023-01-01 18:42:05
 */
@Getter
@Setter
public class ApiProjectDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 分类集合
     */
    private List<ApiCategoryDTO> apiCategoryList;
}
