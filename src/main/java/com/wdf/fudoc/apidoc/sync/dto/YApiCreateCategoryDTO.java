package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-01-06 00:58:11
 */
@Getter
@Setter
public class YApiCreateCategoryDTO {

    /**
     * 项目token
     */
    private String token;


    /**
     * 项目ID
     */
    @JsonProperty("project_id")
    private Long projectId;


    /**
     * 分类id
     */
    @JsonProperty("_id")
    private Integer catId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String desc;
}
