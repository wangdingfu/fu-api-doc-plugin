package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 【Fu Doc】接口同步配置数据
 *
 * @author wangdingfu
 * @date 2022-12-31 22:20:00
 */
@Getter
@Setter
public class FuDocSyncConfigData implements Serializable {

    /**
     * 当前开启的三方接口文档系统 默认开启yapi
     */
    private String enable = getDefault();

    /**
     * yapi配置数据
     */
    private YapiConfigData yapi = new YapiConfigData();

    /**
     * showDoc配置数据
     */
    private ShowDocConfigData showDoc = new ShowDocConfigData();

    /**
     * ApiFox配置数据
     */
    private ApiFoxConfigData apiFox = new ApiFoxConfigData();

    /**
     * 初始化默认showDow和Yapi的配置
     */
    public void initData() {

    }


    public String getDefault() {
        return ApiDocSystem.API_FOX.getCode();
    }


    public BaseSyncConfigData getEnableConfigData() {
        ApiDocSystem instance;
        if (Objects.nonNull(instance = ApiDocSystem.getInstance(this.enable))) {
            switch (instance) {
                case YAPI -> {
                    return this.yapi;
                }
                case SHOW_DOC -> {
                    return this.showDoc;
                }
                case API_FOX -> {
                    return this.apiFox;
                }
            }
        }
        return this.apiFox;
    }


}
