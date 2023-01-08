package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * api的层级结构树
 *
 * @author wangdingfu
 * @date 2023-01-01 21:01:59
 */
@Getter
@Setter
public class ApiStructureTreeDTO {

    /**
     * 项目集合
     */
    private List<ApiProjectDTO> apiProjectList;


    /**
     * 当前项目的配置
     */
    private ApiProjectDTO current;

}
