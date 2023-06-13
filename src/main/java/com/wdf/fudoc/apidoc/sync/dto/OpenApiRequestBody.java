package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-06-12 18:15:24
 */
@Getter
@Setter
public class OpenApiRequestBody {

    /**
     * 接口请求body
     */
    private Map<String, OpenApiContentDTO> content;
}
