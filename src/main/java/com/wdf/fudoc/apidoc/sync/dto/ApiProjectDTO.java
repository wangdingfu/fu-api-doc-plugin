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
     * 分组ID
     */
    private String groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目token
     */
    private String projectToken;


    /**
     * 选中的分类
     */
    private ApiCategoryDTO selectCategory;


    private String applicationName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 分类集合
     */
    private List<ApiCategoryDTO> apiCategoryList;

    private String title;

    @Override
    public String toString() {
        return this.projectName;
    }
}
