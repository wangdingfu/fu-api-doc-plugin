package com.wdf.fudoc.apidoc.pojo.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-09-27 17:23:09
 */
@Getter
@Setter
public class CommonItemData {

    /**
     * 接口唯一标识
     */
    private String apiKey;

    /**
     * 接口标题
     */
    private String title;


    /**
     * 接口详情信息
     */
    private String detailInfo;

    /**
     * 接口请求地址
     */
    private List<String> urlList;

    /**
     * 接口请求类型
     */
    private String requestType;

    /**
     * 请求内容类型
     */
    private ContentType contentType;
}
