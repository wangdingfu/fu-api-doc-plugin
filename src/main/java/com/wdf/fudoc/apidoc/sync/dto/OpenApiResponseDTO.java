package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-06-13 18:42:06
 */
@Getter
@Setter
public class OpenApiResponseDTO {

    /**
     * 响应描述
     */
    private String description;

    /**
     * 接口请求body
     */
    private Map<String, OpenApiContentDTO> content;
}
