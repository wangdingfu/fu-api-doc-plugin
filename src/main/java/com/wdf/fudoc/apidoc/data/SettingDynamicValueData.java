package com.wdf.fudoc.apidoc.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置动态取值参数对象
 * @author wangdingfu
 * @date 2022-08-06 22:45:21
 */
@Getter
@Setter
public class SettingDynamicValueData {

    /**
     * 别名（为配置的这个值取一个名称 在渲染的时候使用）
     */
    private String alias;

    /**
     * 类型 默认注解 annotation-注解
     */
    private String type = "annotation";

    /**
     * 配置取值的内容
     * 例如 com.wdf.annotation.Permission#code
     */
    private String value;
}
