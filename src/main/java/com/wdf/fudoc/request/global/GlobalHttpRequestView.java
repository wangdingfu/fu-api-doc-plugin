package com.wdf.fudoc.request.global;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.view.HttpDialogView;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-09-28 14:26:16
 */
public class GlobalHttpRequestView {

    private static final Map<Project, HttpDialogView> httpDialogViewMap = new ConcurrentHashMap<>();

    public static HttpDialogView getHttpDialogView(Project project) {
        return httpDialogViewMap.get(project);
    }

    public static void addHttpDialogView(Project project, HttpDialogView httpDialogView) {

        httpDialogViewMap.put(project, httpDialogView);
    }

    public static void remove(Project project) {
        httpDialogViewMap.remove(project);
    }
}
