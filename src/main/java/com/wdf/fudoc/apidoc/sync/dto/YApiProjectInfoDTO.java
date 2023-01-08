package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-01-07 02:43:25
 */
@Getter
@Setter
public class YApiProjectInfoDTO {


    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("_id")
    private Integer projectId;

    @JsonProperty("name")
    private String projectName;

}
