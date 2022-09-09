package com.wdf.fudoc.apidoc.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 枚举解析出来的值
 * @author wangdingfu
 * @date 2022-07-29 16:05:05
 */
@Getter
@Setter
@AllArgsConstructor
public class FuDocEnumItemData {

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 枚举描述信息
     */
    private String msg;
}
