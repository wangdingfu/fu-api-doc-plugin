package com.wdf.fudoc.apilist.strategy;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apilist.constant.GroupType;
import com.wdf.fudoc.apilist.pojo.ApiListGroup;
import com.wdf.fudoc.apilist.pojo.ApiListItem;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按 URL 前缀分组策略
 * 根据 URL 的第一级路径进行分组,例如:
 * - /api/user/list -> /api
 * - /user/info -> /user
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
public class PrefixGroupStrategy implements ApiGroupStrategy {

    @Override
    public List<ApiListGroup> group(List<ApiListItem> apiList) {
        if (CollectionUtils.isEmpty(apiList)) {
            return Lists.newArrayList();
        }

        // 按 urlPrefix 分组
        Map<String, List<ApiListItem>> groupMap = apiList.stream()
                .collect(Collectors.groupingBy(
                        ApiListItem::getUrlPrefix,
                        Collectors.toList()
                ));

        // 转换为 ApiListGroup 列表
        List<ApiListGroup> groups = Lists.newArrayList();
        groupMap.forEach((prefix, items) -> {
            ApiListGroup group = new ApiListGroup(prefix, GroupType.PREFIX);
            items.forEach(group::addItem);
            groups.add(group);
        });

        // 按分组名称排序
        groups.sort((g1, g2) -> g1.getGroupName().compareTo(g2.getGroupName()));

        return groups;
    }
}
