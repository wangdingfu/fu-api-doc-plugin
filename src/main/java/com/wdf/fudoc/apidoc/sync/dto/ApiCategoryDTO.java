package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 接口分类
 *
 * @author wangdingfu
 * @date 2023-01-01 18:42:13
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiCategoryDTO implements Serializable {

    /**
     * 分类ID
     */
    @JsonProperty("_id")
    private String categoryId;

    /**
     * 分类名称
     */
    @JsonProperty("name")
    private String categoryName;

    /**
     * 排序字段-根据最近使用日期排序
     */
    private Integer sort;

    /**
     * 父节点
     */
    private transient ApiCategoryDTO parent;

    /**
     * 子分类
     */
    private List<ApiCategoryDTO> apiCategoryList;

    public ApiCategoryDTO(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ApiCategoryDTO(String categoryName, ApiCategoryDTO parent) {
        this.categoryName = categoryName;
        this.parent = parent;
    }

    public ApiCategoryDTO(String categoryId, List<ApiCategoryDTO> apiCategoryList) {
        this.categoryId = categoryId;
        this.apiCategoryList = apiCategoryList;
    }

    public ApiCategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return this.categoryName;
    }
}
