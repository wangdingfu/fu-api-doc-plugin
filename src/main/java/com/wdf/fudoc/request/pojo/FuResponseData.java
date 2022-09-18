package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * http请求响应结果
 *
 * @author wangdingfu
 * @date 2022-09-18 13:53:12
 */
@Getter
@Setter
public class FuResponseData {

    /**
     * 相应内容
     */
    private String content;

    /**
     * 相应头
     */
    private List<KeyValueTableBO> headers;
}
