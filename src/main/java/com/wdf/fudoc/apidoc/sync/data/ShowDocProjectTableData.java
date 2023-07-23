package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangdingfu
 * @date 2023-07-05 19:00:55
 */
@Getter
@Setter
public class ShowDocProjectTableData implements Serializable {

    /**
     * 是否选中
     */
    private Boolean select = true;

    /**
     * 项目token
     */
    private String apiToken;

    /**
     * api key
     */
    private String apiKey;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目范围
     */
    private String applicationName;

    /**
     * 分类集合
     */
    private String categories;
}
