package com.wdf.fudoc.apidoc.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Yapi保存接口对象
 *
 * @author wangdingfu
 * @date 2023-01-05 15:18:50
 */
@Getter
@Setter
public class YApiSaveDTO implements Serializable {

    /**
     * 项目 token
     */
    private String token;

    /**
     * 项目 id
     */
    private Long projectId;

    /**
     * 接口 id
     */
    private String id;

    /**
     * 品类id
     */
    @JsonProperty("catid")
    private Long catId;

    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求数据类型
     * 枚举: raw,form,json
     */
    @JsonProperty("req_body_type")
    private String reqBodyType;

    /**
     * 请求数据body
     */
    @JsonProperty("req_body_other")
    private String reqBodyOther;

    /**
     * 请求参数body 是否为json_schema
     */
    @JsonProperty("req_body_is_json_schema")
    private boolean reqBodyIsJsonSchema;


    /**
     * 请求参数(form-data)
     */
    @JsonProperty("req_body_form")
    private List<YApiParamDTO> reqBodyForm = Lists.newArrayList();

    /**
     * 请求参数(urlencoded)
     */
    @JsonProperty("req_params")
    private List<YApiParamDTO> reqParams = Lists.newArrayList();

    /**
     * 请求头
     */
    @JsonProperty("req_headers")
    private List<YApiHeaderDTO> reqHeaders = Lists.newArrayList();

    /**
     * 查询参数(GET请求)
     */
    @JsonProperty("req_query")
    private List<YApiParamDTO> reqQuery = Lists.newArrayList();

    /**
     * 返回参数类型  json
     * 枚举: raw,json
     */
    @JsonProperty("res_body_type")
    private String resBodyType = "json";

    /**
     * 返回参数
     */
    @JsonProperty("res_body")
    private String resBody;


    /**
     * 文档描述
     */
    private String desc = "";

    /**
     * 标题
     */
    private String title;
    /**
     * 邮件开关
     */
    @JsonProperty("switch_notice")
    private Boolean switchNotice = false;

    /**
     * 状态 undone,默认done
     */
    private String status = "undone";


    /**
     * 返回参数是否为json_schema
     */
    @JsonProperty("res_body_is_json_schema")
    private boolean resBodyIsJsonSchema = true;

    /**
     * 备注信息
     */
    private String markdown;
}
