package com.wdf.fudoc.apidoc.sync.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 第三方接口文档系统项目配置
 *
 * @author wangdingfu
 * @date 2023-01-01 13:40:07
 */
@Getter
@Setter
public class ApiSystemProjectConfigData {

    /**
     * 分组ID（showDoc系统的groupId默认为default）
     */
    private String groupId;

    /**
     * 第三方接口文档系统项目id
     */
    private String projectId;

    /**
     * 第三方接口文档系统项目名称
     */
    private String projectName;


    /**
     * 自己代码中的java module集合（标识这些module下的接口文档都归属到该项目下）
     */
    private List<String> moduleIdList;

    /**
     * 接口挂载的菜单和项目接口的映射关系
     * key:接口挂载的菜单全路径  value:接口所属的目录集合
     */
    private Map<String, List<String>> apiCategoryMapping;


}
