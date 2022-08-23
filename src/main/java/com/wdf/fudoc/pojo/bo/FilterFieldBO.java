package com.wdf.fudoc.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-08-12 21:49:29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterFieldBO {

    /**
     * 类名
     */
    private String className;

    /**
     * 字段名 多个用","号分隔
     */
    private String fieldNames;
}
