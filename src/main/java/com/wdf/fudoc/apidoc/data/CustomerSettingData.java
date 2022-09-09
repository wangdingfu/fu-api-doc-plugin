package com.wdf.fudoc.apidoc.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.config.DefaultConfig;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.apidoc.pojo.bo.SettingEnumBO;
import com.wdf.fudoc.apidoc.pojo.bo.SettingValidBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 自定义配置数据
 *
 * @author wangdingfu
 * @date 2022-08-07 01:13:36
 */
@Getter
@Setter
public class CustomerSettingData {
    /**
     * 自定义取值逻辑配置
     */
    private List<SettingDynamicValueData> setting_customer_value = Lists.newArrayList();

    /**
     * 自定义过滤字段
     */
    private List<FilterFieldBO> settings_filter_field = Lists.newArrayList(DefaultConfig.DEFAULT_FILTER_FIELD_LIST);

    /**
     * 校验注解相关配置
     */
    private SettingValidBO setting_valid = new SettingValidBO();

    /**
     * 枚举相关配置
     */
    private SettingEnumBO setting_enum = new SettingEnumBO();


    public CustomerSettingData defaultValue() {
        if (Objects.isNull(this.settings_filter_field)) {
            this.settings_filter_field = Lists.newArrayList(DefaultConfig.DEFAULT_FILTER_FIELD_LIST);
        }
        return this;
    }

}
