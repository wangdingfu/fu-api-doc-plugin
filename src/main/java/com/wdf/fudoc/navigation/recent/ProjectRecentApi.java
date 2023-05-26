package com.wdf.fudoc.navigation.recent;

import com.google.common.collect.Lists;
import com.intellij.ide.actions.searcheverywhere.FoundItemDescriptor;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.storage.FuStorageAppender;
import kotlin.Pair;
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
    private final List<String> historyUrlList;

    private final Map<String, ApiNavigationItem> historyMap = new ConcurrentHashMap<>();

    public void add(ApiNavigationItem apiNavigationItem) {
        String url = apiNavigationItem.getUrl();
        appender.append(url);
        historyUrlList.add(url);
        historyMap.put(url, apiNavigationItem);
    }


    public void initAdd(ApiNavigationItem apiNavigationItem) {
        String url = apiNavigationItem.getUrl();
        if (historyUrlList.contains(url)) {
            historyMap.put(url, apiNavigationItem);
        }
    }


    public List<FoundItemDescriptor<ApiNavigationItem>> historyList() {
        List<FoundItemDescriptor<ApiNavigationItem>> apiList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(this.historyUrlList)) {
            for (int i = historyUrlList.size() - 1; i >= 0; i--) {
                String url = historyUrlList.get(i);
                ApiNavigationItem apiNavigationItem = historyMap.get(url);
                if (Objects.isNull(apiNavigationItem)) {
                    continue;
                }
                apiList.add(new FoundItemDescriptor<>(apiNavigationItem, 0));
            }
        }
        return apiList;
    }


    public ProjectRecentApi(Project project) {
        this.appender = FuStorageAppender.getInstance(project, FILE_NAME, "config");
        this.historyUrlList = this.appender.read();
    }


}
