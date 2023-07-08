package com.wdf.fudoc.request.tab.request;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuConsole;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import icons.FuDocIcons;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-07 11:38:21
 */
public class RequestConsoleTabView implements FuTab, HttpCallback {

    private final FuConsole fuConsole;
    private final JPanel slidePanel;

    public RequestConsoleTabView(Project project, JPanel slidePanel, Disposable disposable) {
        this.slidePanel = slidePanel;
        ConsoleViewImpl consoleView = new ConsoleViewImpl(project, true);
        Disposer.register(disposable, consoleView);
        this.fuConsole = new FuConsole(consoleView);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Console", FuDocIcons.CONSOLE, fuConsole.getComponent()).builder();
    }

    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        if(Objects.nonNull(this.slidePanel)){
            newSelection.setSideComponent(this.slidePanel);
        }
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        fuConsole.clear();
    }


    public FuConsole console() {
        return this.fuConsole;
    }
}
