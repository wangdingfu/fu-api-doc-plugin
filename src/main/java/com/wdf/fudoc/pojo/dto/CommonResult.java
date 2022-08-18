package com.wdf.fudoc.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author wangdingfu
 * @date 2022-08-18 11:14:05
 */
@Getter
@Setter
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = -504632043702611889L;
    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应结果
     */
    private T data;

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应时间戳
     */
    private long timestamp;

    private CommonResult() {

    }

    public static <T> CommonResult<T> ok() {
        return ok(null);
    }

    public static <T> CommonResult<T> ok(T data) {
        return ok(200, "请求成功", data);
    }

    public static <T> CommonResult<T> ok(Integer code, String msg, T data) {
        return create(code, msg, data);
    }

    public static <T> CommonResult<T> fail() {
        return fail(null);
    }

    public static <T> CommonResult<T> fail(T data) {
        return fail(-1, "请求失败", data);
    }

    public static <T> CommonResult<T> fail(Integer code, String msg, T data) {
        return create(code, msg, data);
    }

    public static <T> CommonResult<T> create(Integer code, String msg, T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        result.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).getSecond());
        return result;
    }
}
