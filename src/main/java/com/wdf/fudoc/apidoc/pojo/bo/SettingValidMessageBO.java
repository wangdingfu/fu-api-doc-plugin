package com.wdf.fudoc.apidoc.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Valid注解message属性相关配置
 *
 * @author wangdingfu
 * @date 2022-08-15 20:34:48
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingValidMessageBO {

    /**
     * 类型
     */
    private String type;

    /**
     * 值
     */
    private String value;
}
