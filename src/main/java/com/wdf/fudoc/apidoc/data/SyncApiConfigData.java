package com.wdf.fudoc.apidoc.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-08 20:35:29
 */
@Setter
public class SyncApiConfigData {
    /**
     * YApi项目配置
     */
    private List<YApiProjectTableData> yapiConfigList;


    public List<YApiProjectTableData> getYapiConfigList() {
        if(Objects.isNull(this.yapiConfigList)){
            return Lists.newArrayList();
        }
        return this.yapiConfigList;
    }
}
