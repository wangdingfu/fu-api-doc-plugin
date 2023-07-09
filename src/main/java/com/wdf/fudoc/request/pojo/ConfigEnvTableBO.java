package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.DynamicTableBO;
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

}
