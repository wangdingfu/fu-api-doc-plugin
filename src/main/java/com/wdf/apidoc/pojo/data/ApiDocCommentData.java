package com.wdf.apidoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @description 注释数据对象
 * @Date 2022-04-21 14:11:36
 */
@Getter
@Setter
public class ApiDocCommentData {

    /**
     * 注释标题
     */
    private String commentTitle;

    /**
     * 注释详细描述信息
     */
    private List<String> commentDescList;

    /**
     * 参数注释map
     * key:参数名  value:参数对应的注释
     */
    private Map<String, String> paramCommentMap;

    /**
     * 返回注释内容
     */
    private String returnComment;

}
