package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-06-13 09:58:06
 */
@Getter
@Setter
public class ApiFoxDTO {

    private String importFormat;

    private String apiOverwriteMode;

    private OpenApiDTO data;

    private boolean syncApiFolder;
}
