package com.wdf.fudoc.test.action;

import cn.hutool.core.util.RandomUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.navigation.FuApiNavigationExecutor;
import com.wdf.fudoc.navigation.recent.ProjectRecentApi;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
//import com.wdf.fudoc.start.FuDocStartUpListener;
import com.wdf.fudoc.util.FuRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Slf4j
public class TestAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(TestAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        FuDocStartUpListener.statisticsAction("test", false);
    }


    public void showPanel() {
        KeyValueTableBO keyValueTableBO = new KeyValueTableBO();
    }


    private void apiTest(AnActionEvent e) {
        ProjectRecentApi projectRecentApi = RecentNavigationManager.create(e.getProject());
        long start = System.currentTimeMillis();
        List<ApiNavigationItem> apiList = FuApiNavigationExecutor.getInstance(e.getProject(), projectRecentApi).getApiList();
        System.out.println("读取api【" + apiList.size() + "】条 共计耗时:" + (System.currentTimeMillis() - start) + "ms");
    }



}
