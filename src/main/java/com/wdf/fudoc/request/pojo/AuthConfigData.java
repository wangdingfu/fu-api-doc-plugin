package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.BaseList;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-02-21 00:03:23
 */
@Getter
@Setter
public class AuthConfigData extends BaseList {

    /**
     * 鉴权请求数据对象
     */
    private FuHttpRequestData httpRequestData;


    /**
     * 权限配置
     */
    private Map<String, BaseAuthConfig> authConfigMap;


    public BaseAuthConfig getAuthConfig(String tab) {
        if (MapUtils.isNotEmpty(authConfigMap)) {
            return authConfigMap.get(tab);
        }
        return null;
    }

    public void addAuthConfig(String name, BaseAuthConfig baseAuthConfig) {
        if (Objects.isNull(this.authConfigMap)) {
            this.authConfigMap = new ConcurrentHashMap<>();
        }
        this.authConfigMap.put(name, baseAuthConfig);
    }
}
