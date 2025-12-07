package com.wdf.fudoc.apilist.strategy;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apilist.constant.GroupType;
import com.wdf.fudoc.apilist.pojo.ApiListGroup;
import com.wdf.fudoc.apilist.pojo.ApiListItem;
import com.wdf.fudoc.util.FuStringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按 Module/Class 分组策略
 * 按照 "Module名称/类名" 进行分组
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
public class ModuleGroupStrategy implements ApiGroupStrategy {

    @Override
    public List<ApiListGroup> group(List<ApiListItem> apiList) {
        if (CollectionUtils.isEmpty(apiList)) {
            return Lists.newArrayList();
        }

        // 按 "moduleName/className" 分组
        Map<String, List<ApiListItem>> groupMap = apiList.stream()
                .collect(Collectors.groupingBy(
                        this::buildGroupKey,
                        Collectors.toList()
                ));

        // 转换为 ApiListGroup 列表
        List<ApiListGroup> groups = Lists.newArrayList();
        groupMap.forEach((groupKey, items) -> {
            ApiListGroup group = new ApiListGroup(groupKey, GroupType.MODULE);
            items.forEach(group::addItem);
            groups.add(group);
        });

        // 按分组名称排序
        groups.sort((g1, g2) -> g1.getGroupName().compareTo(g2.getGroupName()));

        return groups;
    }

    /**
     * 构建分组键: Module名称/类名
     * 例如: "app-module/UserController"
     */
    private String buildGroupKey(ApiListItem api) {
        String moduleName = FuStringUtils.isNotBlank(api.getModuleName()) ? api.getModuleName() : "Unknown";
        String className = getSimpleClassName(api.getClassName());
        return moduleName + "/" + className;
    }

    /**
     * 获取简单类名(不含包名)
     * 例如: "com.example.controller.UserController" -> "UserController"
     */
    private String getSimpleClassName(String fullClassName) {
        if (FuStringUtils.isBlank(fullClassName)) {
            return "Unknown";
        }
        int lastDot = fullClassName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fullClassName.length() - 1) {
            return fullClassName.substring(lastDot + 1);
        }
        return fullClassName;
    }
}
