package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.common.base.KeyValueBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-07-14 14:04:16
 */
@Getter
@Setter
public class FuResponseConsoleData {

    /**
     * 请求类型
     */
    private String httpType;

    /**
     * http响应码
     */
    private int httpStatus;

    /**
     * 请求头
     */
    private List<KeyValueBO> headers;

    /**
     * 请求body
     */
    private String responseBody;


}
