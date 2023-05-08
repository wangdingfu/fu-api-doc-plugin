package com.wdf.fudoc.apidoc.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-05-08 20:35:29
 */
@Getter
@Setter
public class SyncApiConfigData {
    /**
     * YApi项目配置
     */
    private List<YApiProjectTableData> yapiConfigList = Lists.newArrayList();
}
