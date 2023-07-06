package com.wdf.fudoc.apidoc.sync.service;

import com.wdf.fudoc.apidoc.sync.data.ApiFoxConfigData;
import com.wdf.fudoc.apidoc.sync.data.ShowDocConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocDTO;

/**
 * @author wangdingfu
 * @date 2023-07-05 20:13:02
 */
public interface ShowDocService {


    String syncApi(ShowDocDTO showDocDTO, ShowDocConfigData showDocConfigData);
}
