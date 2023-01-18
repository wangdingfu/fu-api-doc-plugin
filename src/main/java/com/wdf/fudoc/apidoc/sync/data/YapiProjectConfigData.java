package com.wdf.fudoc.apidoc.sync.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Yapi项目配置
 *
 * @author wangdingfu
 * @date 2023-01-05 13:33:11
 */
@Getter
@Setter
public class YapiProjectConfigData {

    /**
     * 项目token
     */
    private String projectToken;

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
     * 对应代码中的module模块集合（表示将这些module的接口同步到Yapi的这个项目中）
     */
    private List<String> moduleIdList;
}
