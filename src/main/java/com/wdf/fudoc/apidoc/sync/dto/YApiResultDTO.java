package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * YApi返回的结果
 *
 * @author wangdingfu
 * @date 2023-01-09 17:35:46
 */
@Getter
@Setter
public class YApiResultDTO {

    /**
     * 1-成功
     */
    private Integer ok;

    /**
     * 1-编辑成功
     */
    private Integer nModified;
}
