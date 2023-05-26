package com.wdf.fudoc.navigation.recent;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.navigation.ApiNavigationItem;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-05-26 15:36:17
 */
public class RecentNavigationManager {


    private static final Map<Project, ProjectRecentApi> RECENT_NAVIGATION_MAP = new ConcurrentHashMap<>();

    public static ProjectRecentApi create(Project project) {
        ProjectRecentApi projectRecent = RECENT_NAVIGATION_MAP.get(project);
        if (Objects.isNull(projectRecent)) {
            projectRecent = new ProjectRecentApi(project);
            RECENT_NAVIGATION_MAP.put(project, projectRecent);
        }
        return projectRecent;
    }

    public static void add(Project project, ApiNavigationItem apiNavigationItem) {
        ProjectRecentApi projectRecent = RECENT_NAVIGATION_MAP.get(project);
        if (Objects.isNull(projectRecent)) {
            projectRecent = create(project);
        }
        projectRecent.add(apiNavigationItem);
    }


}
