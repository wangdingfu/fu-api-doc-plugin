package com.wdf.fudoc.apidoc.sync.service;

import com.wdf.fudoc.apidoc.sync.data.ApiFoxConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiFoxDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;

/**
 * @author wangdingfu
 * @date 2023-06-13 17:14:49
 */
public interface ApiFoxService {

    /**
     * 同步API至ApiFox系统
     *
     * @param apiFoxDTO        同步参数
     * @param apiFoxConfigData 配置数据
     */
    void syncApi(ApiFoxDTO apiFoxDTO, ApiProjectDTO apiProjectDTO, ApiFoxConfigData apiFoxConfigData);
}
