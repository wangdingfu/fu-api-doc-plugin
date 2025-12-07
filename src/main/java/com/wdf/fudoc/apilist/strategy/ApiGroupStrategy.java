package com.wdf.fudoc.apilist.strategy;

import com.wdf.fudoc.apilist.pojo.ApiListGroup;
import com.wdf.fudoc.apilist.pojo.ApiListItem;

import java.util.List;

/**
 * API 分组策略接口
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
public interface ApiGroupStrategy {

    /**
     * 对 API 列表进行分组
     *
     * @param apiList API 列表
     * @return 分组后的结果
     */
    List<ApiListGroup> group(List<ApiListItem> apiList);
}
