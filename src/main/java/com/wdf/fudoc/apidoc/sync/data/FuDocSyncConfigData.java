package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 【Fu Doc】接口同步配置数据
 *
 * @author wangdingfu
 * @date 2022-12-31 22:20:00
 */
@Getter
@Setter
public class FuDocSyncConfigData {

    /**
     * 当前开启的三方接口文档系统
     */
    private String enable;


    /**
     * 同步配置数据集合
     */
    private Map<String, BaseSyncConfigData> syncConfigDataMap = new HashMap<>();





    /**
     * 初始化默认showDow和Yapi的配置
     */
    public void initData() {

    }


    public BaseSyncConfigData getEnableConfigData() {
        if (StringUtils.isBlank(this.enable)) {
            //获取默认的配置
            this.enable = ApiDocSystem.YAPI.getCode();
        }
        return this.syncConfigDataMap.get(enable);
    }


}
