package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 项目分组对象（一个分组会有多个项目）
 *
 * @author wangdingfu
 * @date 2023-01-01 18:41:57
 */
@Getter
@Setter
public class ApiGroupDTO {

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 项目集合
     */
    private List<ApiProjectDTO> apiProjectList;
}
