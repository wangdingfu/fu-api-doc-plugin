package com.wdf.fudoc.apidoc.sync.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wdf.fudoc.apidoc.sync.data.SyncApiRecordData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-01-11 20:28:40
 */
@Getter
@Setter
public class ProjectSyncApiRecordData implements Serializable {


    /**
     * 接口的同步记录
     */
    private final Map<String, SyncApiRecordData> syncApiRecordMap = new ConcurrentHashMap<>();


    /**
     * key:接口文档系统的项目名称 value:当前项目下分类集合 按照最近使用顺序排列
     */
    private final Map<String, List<String>> projectCategoryMap = new ConcurrentHashMap<>();


    /**
     * 是否存在该记录
     *
     * @param url 接口地址
     * @return true 存在
     */
    public boolean exists(String url) {
        SyncApiRecordData recordData = syncApiRecordMap.get(url);
        return Objects.nonNull(recordData) && Objects.nonNull(recordData.getCategory()) && StringUtils.isNotBlank(recordData.getProjectName()) && StringUtils.isNotBlank(recordData.getProjectId());
    }

    /**
     * 根据接口地址获取该接口的同步记录
     *
     * @param url 接口地址
     * @return 接口同步记录
     */
    public SyncApiRecordData getRecord(String url) {
        return syncApiRecordMap.get(url);
    }

    /**
     * 添加同步记录
     *
     * @param record 同步记录
     */
    public void addRecord(SyncApiRecordData record) {
        if (Objects.nonNull(record) && StringUtils.isNotBlank(record.getApiUrl())) {
            this.syncApiRecordMap.put(record.getApiUrl(), record);
        }
    }


    public List<String> getCategoryList(String projectName) {
        if (StringUtils.isNotBlank(projectName)) {
            return projectCategoryMap.get(projectName);
        }
        return Lists.newArrayList();
    }


    /**
     * 添加分类
     *
     * @param projectName 项目名称
     * @param category    分类名称
     */
    public void addCategory(String projectName, String category) {
        if (StringUtils.isBlank(category) || StringUtils.isBlank(projectName)) {
            return;
        }
        List<String> categoryList = projectCategoryMap.get(projectName);
        if (Objects.isNull(categoryList)) {
            categoryList = Lists.newArrayList();
            projectCategoryMap.put(projectName, categoryList);
        }
        categoryList.remove(category);
        categoryList.add(category);
    }
}
