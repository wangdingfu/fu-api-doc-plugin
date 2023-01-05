package com.wdf.fudoc.apidoc.sync.data;


import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.dto.ApiTreeKeyDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 同步接口表格数据对象
 *
 * @author wangdingfu
 * @date 2023-01-01 18:22:16
 */
@Getter
@Setter
public class SyncApiTableData extends ApiTreeKeyDTO {

    /**
     * 接口唯一标识
     */
    private String apiKey;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口地址
     */
    private String apiUrl;

    /**
     * 接口文档数据
     */
    private FuDocItemData apiData;

    /**
     * 同步状态
     */
    private String syncStatus;

}
