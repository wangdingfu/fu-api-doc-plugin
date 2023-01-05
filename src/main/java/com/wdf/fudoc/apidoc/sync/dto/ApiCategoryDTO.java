package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ApiCategoryDTO {

    /**
     * 分类ID
     */
    private String categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 子分类
     */
    private List<ApiCategoryDTO> apiCategoryList;

    public ApiCategoryDTO(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
