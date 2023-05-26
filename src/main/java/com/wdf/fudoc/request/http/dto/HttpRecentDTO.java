package com.wdf.fudoc.request.http.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-05-22 13:46:36
 */
@Getter
@Setter
public class HttpRecentDTO {

    private String apiName;

    private String controllerPkg;

    private String methodName;

    private String mappingUrl;
}
