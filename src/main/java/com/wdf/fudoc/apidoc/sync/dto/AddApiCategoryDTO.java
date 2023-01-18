package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-01-06 01:04:09
 */
@Getter
@Setter
public class AddApiCategoryDTO {

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
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类描述
     */
    private String categoryDesc;
}
