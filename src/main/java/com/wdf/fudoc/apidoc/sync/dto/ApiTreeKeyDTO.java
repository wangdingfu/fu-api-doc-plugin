package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-01-01 22:30:51
 */
@Getter
@Setter
public class ApiTreeKeyDTO {

    /**
     * 分组id
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
     * 接口分类id
     */
    private String categoryId;

    /**
     * 接口分类名称
     */
    private String categoryName;


    public String getApiTreeKey() {
        return this.groupName + ":" + this.projectName + "" + this.categoryName;
    }
}
