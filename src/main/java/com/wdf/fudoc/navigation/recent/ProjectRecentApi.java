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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        this.historyUrlList = readApiLogList();
    }


    private List<RecentApiLog> readApiLogList() {
        List<String> navigationLogList = this.appender.read();
        if (CollectionUtils.isEmpty(navigationLogList)) {
            return Lists.newArrayList();
        }
        if (navigationLogList.size() <= FuDocConstants.API_NAVIGATION_LIMIT) {
            //数量小于100条 不处理 直接放入历史记录中
            return ObjectUtils.listToList(navigationLogList, RecentApiLog::new);
        }
        List<RecentApiLog> recentList = Lists.newArrayList();
        //从最后一条（即最近一条导航记录）开始遍历 只取最近100条api导航记录
        for (int i = navigationLogList.size() - 1; i >= 0; i--) {
            if (recentList.size() >= 100) {
                break;
            }
            RecentApiLog recentApiLog = new RecentApiLog(navigationLogList.get(i));
            //判断是否有重复url
            if (recentList.stream().noneMatch(a -> a.getUrl().equals(recentApiLog.getUrl()))) {
                recentList.add(recentApiLog);
            }
        }
        //根据时间排序
        recentList.sort(Comparator.comparing(RecentApiLog::getTime));
        //当导航API日志文件条数超过300条 则需要进行去重和优化至最近100条 防止文件过大
        if (navigationLogList.size() > FuDocConstants.API_NAVIGATION_MAX_LIMIT) {
            ApplicationManager.getApplication().runWriteAction(() -> this.appender.writeAll(recentList.stream().map(RecentApiLog::toString).collect(Collectors.toList())));
        }
        return recentList;
    }


}
