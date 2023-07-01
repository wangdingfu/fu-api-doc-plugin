package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangdingfu
 * @date 2023-07-01 15:00:59
 */
@Getter
@Setter
public class ApiFoxProjectTableData implements Serializable {

    /**
     * 是否选中
     */
    private Boolean select = true;

    /**
     * 项目ID-接口文档系统
     */
    private String projectId;

    /**
     * 项目范围
     */
    private TreePathBO scope;
}
