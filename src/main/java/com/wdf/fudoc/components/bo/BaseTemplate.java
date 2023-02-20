package com.wdf.fudoc.components.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 左右组件数据模板对象
 *
 * @author wangdingfu
 * @date 2023-02-20 22:36:43
 */
@Getter
@Setter
public class BaseTemplate {


    /**
     * 用于标识当前列表的唯一一条记录 并且用于在列表中展示
     */
    private String identify;
}
