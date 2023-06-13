package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-06-12 18:12:38
 */
@Getter
@Setter
public class OpenApiItemDTO {

    /**
     * 接口名称
     */
    private String summary;

    /**
     * 接口目录
     */
    @JsonProperty("x-apifox-folder")
    private String folder;

    /**
     * 接口状态
     */
    @JsonProperty("x-apifox-status")
    private String status;

    /**
     * 接口方法名称
     */
    private String operationId;

    /**
     * 接口描述信息
     */
    private String description;

    /**
     * 接口参数
     */
    private List<OpenApiParameterItemDTO> parameters;

    /**
     * 请求体数据
     */
    private OpenApiRequestBody requestBody;

    /**
     * 响应结果
     */
    private Map<String, OpenApiResponseDTO> responses;
}
