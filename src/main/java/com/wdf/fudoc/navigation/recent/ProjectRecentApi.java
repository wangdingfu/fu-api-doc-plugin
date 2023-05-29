package com.wdf.fudoc.navigation.recent;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.storage.FuStorageAppender;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.TimeFormatUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-05-26 17:05:39
 */
public class ProjectRecentApi {
    private static final String FILE_NAME = "navigation.log";
    /**
     * 文件内容追加
     */
    private final FuStorageAppender appender;
    /**
     * 历史搜索的url
     */
    private final List<RecentApiLog> historyUrlList;

    private final Map<String, ApiNavigationItem> historyMap = new ConcurrentHashMap<>();

    public void add(ApiNavigationItem apiNavigationItem) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            String url = apiNavigationItem.getUrl();
            historyUrlList.removeIf(f -> f.getUrl().equals(url));
            long currentSeconds = DateUtil.currentSeconds();
            appender.append(url + "|" + currentSeconds);
            historyUrlList.add(new RecentApiLog(url, currentSeconds));
            historyMap.put(url, apiNavigationItem);
        });
    }


    public void initAdd(ApiNavigationItem apiNavigationItem) {
        String url = apiNavigationItem.getUrl();
        if (historyUrlList.stream().anyMatch(a -> a.getUrl().equals(url))) {
            historyMap.put(url, apiNavigationItem);
        }
    }


    public List<FoundItemDescriptor<ApiNavigationItem>> historyList() {
        List<FoundItemDescriptor<ApiNavigationItem>> apiList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(this.historyUrlList)) {
            for (int i = historyUrlList.size() - 1; i >= 0; i--) {
                RecentApiLog recentApiLog = historyUrlList.get(i);
                ApiNavigationItem apiNavigationItem = historyMap.get(recentApiLog.getUrl());
                if (Objects.isNull(apiNavigationItem)) {
                    continue;
                }
                apiNavigationItem.setTimeStr(TimeFormatUtils.format(recentApiLog.getTime()));
                apiList.add(new FoundItemDescriptor<>(apiNavigationItem, 0));
            }
        }
        return apiList;
    }


    public ProjectRecentApi(Project project) {
        this.appender = FuStorageAppender.getInstance(project, FILE_NAME, "config");
        List<String> navigationLogList = this.appender.read();
        int size = navigationLogList.size();
        List<String> apiLogList = Lists.newArrayList();
        //只保存最近100条api导航记录
        for (int i = size - FuDocConstants.API_NAVIGATION_LIMIT; i < size; i++) {
            apiLogList.add(navigationLogList.get(i));
        }
        //当历史搜索记录超过500行时 需要对文件进行重置 避免历史导航记录文件过大
        if (navigationLogList.size() > FuDocConstants.API_NAVIGATION_MAX_LIMIT) {
            ApplicationManager.getApplication().runWriteAction(() -> this.appender.writeAll(apiLogList));
        }
        this.historyUrlList = ObjectUtils.listToList(apiLogList, RecentApiLog::new);
    }


}
