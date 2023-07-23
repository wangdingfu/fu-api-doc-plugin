package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.DynamicTableBO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-08 16:57:31
 */
@Getter
@Setter
public class ConfigAuthTableBO extends DynamicTableBO {

    private Boolean select = true;

    private String userName;

    private String password;
}
