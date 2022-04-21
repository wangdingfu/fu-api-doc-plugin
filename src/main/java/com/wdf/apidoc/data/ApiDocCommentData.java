package com.wdf.apidoc.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption 注释数据对象
 * @Date 2022-04-21 14:11:36
 */
@Getter
@Setter
public class ApiDocCommentData {

    /**
     * 主注释
     */
    private String mainBody;

    /**
     * 参数注释map
     * key:参数名  value:参数对应的注释
     */
    private String paramCommentMap;

    /**
     * 返回注释内容
     */
    private String returnComment;

}
