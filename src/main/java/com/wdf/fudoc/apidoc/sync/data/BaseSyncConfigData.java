package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步到第三方接口文档系统抽象配置
 *
 * @author wangdingfu
 * @date 2022-12-31 22:21:30
 */
@Getter
@Setter
public abstract class BaseSyncConfigData implements Serializable {

    /**
     * 三方接口文档系统地址
     */
    private String baseUrl;

    /**
     * 自动生成分类
     */
    private boolean autoGenCategory;

    /**
     * 接口的同步记录
     */
    private Map<String, SyncApiRecordData> syncApiRecordMap = new ConcurrentHashMap<>();


    public boolean isRecord(String url) {
        return syncApiRecordMap.containsKey(url);
    }

    public SyncApiRecordData getRecord(String url) {
        return syncApiRecordMap.get(url);
    }

    public void addRecord(SyncApiRecordData record) {
        if (Objects.nonNull(record) && StringUtils.isNotBlank(record.getApiUrl())) {
            this.syncApiRecordMap.put(record.getApiUrl(), record);
        }
    }

    /**
     * 获取当前module对应到第三方接口文档的项目名称
     *
     * @param moduleName 代码中的java module名称
     * @return 第三方接口文档系统的项目名称
     */
    public abstract List<ApiProjectDTO> getProjectConfigList(String moduleName);


    /**
     * 是否存在当前项目配置
     */
    public abstract boolean isExistsConfig();

}
