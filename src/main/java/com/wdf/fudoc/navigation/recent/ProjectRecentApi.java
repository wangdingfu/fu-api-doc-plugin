package com.wdf.fudoc.navigation.recent;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.storage.FuStorageAppender;
import com.wdf.fudoc.util.ObjectUtils;
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
        String url = apiNavigationItem.getUrl();
        historyUrlList.removeIf(f -> f.getUrl().equals(url));
        appender.append(url);
        historyUrlList.add(new RecentApiLog(url, DateUtil.currentSeconds()));
        historyMap.put(url, apiNavigationItem);
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
                apiNavigationItem.setTimeStr(recentApiLog.getTime() + "");
                apiList.add(new FoundItemDescriptor<>(apiNavigationItem, 0));
            }
        }
        return apiList;
    }


    public ProjectRecentApi(Project project) {
        this.appender = FuStorageAppender.getInstance(project, FILE_NAME, "config");
        historyUrlList = ObjectUtils.listToList(this.appender.read(), RecentApiLog::new);
    }


}
