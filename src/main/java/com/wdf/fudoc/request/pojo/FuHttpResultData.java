package com.wdf.fudoc.request.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-07-14 14:17:20
 */
@Getter
@Setter
public class FuHttpResultData {

    private String result;

    private int httpStatus;

    private Long time;
}
