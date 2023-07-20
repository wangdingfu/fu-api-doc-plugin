package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:06:57
 */
@Getter
@Setter
public class ConfigEnvTableBO extends DynamicTableBO {

    /**
     * 环境名称
     */
    private String envName;

    /**
     * 域名
     */
    private String domain;

    /**
     * 作用范围
     */
    private TreePathBO scope;

    /**
     * 应用
     */
    private String application;

}
