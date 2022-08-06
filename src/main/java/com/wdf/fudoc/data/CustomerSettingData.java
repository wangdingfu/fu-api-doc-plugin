package com.wdf.fudoc.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 自定义配置数据
 * @author wangdingfu
 * @date 2022-08-07 01:13:36
 */
@Getter
@Setter
public class CustomerSettingData {
    /**
     * 自定义取值逻辑配置
     */
    private List<SettingDynamicValueData> setting_customer_value;
}
