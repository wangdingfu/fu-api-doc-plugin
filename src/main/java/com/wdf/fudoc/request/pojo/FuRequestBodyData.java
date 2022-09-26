package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * http请求body内容
 *
 * @author wangdingfu
 * @date 2022-09-18 13:56:32
 */
@Getter
@Setter
public class FuRequestBodyData {

    /**
     * 请求参数
     */
    private List<KeyValueTableBO> formDataList;

    /**
     * 请求参数
     */
    private List<KeyValueTableBO> formUrlEncodedList;

    /**
     * json内容
     */
    private String json;

    /**
     * 请求文本
     */
    private String raw;

    /**
     * 请求字节流
     */
    private byte[] binary;
}
