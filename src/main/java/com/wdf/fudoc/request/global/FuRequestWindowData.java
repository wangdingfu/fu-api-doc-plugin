package com.wdf.fudoc.request.global;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-12-05 22:59:05
 */
public class FuRequestWindowData {


    private static final Map<Project, FuRequestWindow> FU_REQUEST_WINDOW_MAP = new ConcurrentHashMap<>();


    public static FuRequestWindow create(Project project, ToolWindow toolWindow) {
        FuRequestWindow fuRequestWindow = FU_REQUEST_WINDOW_MAP.get(project);
        if (Objects.isNull(fuRequestWindow)) {
            fuRequestWindow = new FuRequestWindow(project, toolWindow);
            FU_REQUEST_WINDOW_MAP.put(project, fuRequestWindow);
        }
        return fuRequestWindow;
    }


    public static FuRequestWindow get(Project project) {
        return FU_REQUEST_WINDOW_MAP.get(project);
    }
}
