package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-05 20:16:01
 */
@Getter
@Setter
public class ShowDocDTO {

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("api_token")
    private String apiToken;

    @JsonProperty("cat_name")
    private String categoryName;

    @JsonProperty("page_title")
    private String title;

    @JsonProperty("page_content")
    private String content;

    @JsonProperty("s_number")
    private Integer sort;
}
