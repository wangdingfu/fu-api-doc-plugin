package com.wdf.fudoc.request.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 前置操作表格对象
 *
 * @author wangdingfu
 * @date 2022-12-26 22:43:04
 */
@Getter
@Setter
public class PreOperationTableBO {

    /**
     * 操作名称
     */
    private String name;

    /**
     * 操作类型
     */
    private String type;


    /**
     * 是否启用
     */
    private boolean select;


}
