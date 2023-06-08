package com.wdf.fudoc.request.tab.settings;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuCmdComponent;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.util.ResourceUtils;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * 前置操作tab
 *
 * @author wangdingfu
 * @date 2022-12-26 22:38:48
 */
public class GlobalPreScriptTab implements FuDataTab<FuRequestConfigPO>, FuActionListener<ScriptCmd> {


    private final JPanel rootPanel;

    private final Project project;

    private final JPanel leftPanel;

    private final JPanel emptyPanel;

    private final FuEditorComponent fuEditorComponent;

    private boolean isEditor = false;

    public GlobalPreScriptTab(Project project) {
        this.project = project;
        this.rootPanel = new JPanel(new BorderLayout());
        this.emptyPanel = FuEditorEmptyTextPainter.createFramePreview();
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE);
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(this.emptyPanel, BorderLayout.CENTER);
        FuCmdComponent instance = FuCmdComponent.getInstance(this.fuEditorComponent, this);
        ScriptCmd.execute((cmdType, list) -> instance.addCmd(cmdType.getDesc(), list));
        Splitter splitter = new Splitter(false, 0.7F);
        splitter.setFirstComponent(this.leftPanel);
        splitter.setSecondComponent(instance.getRootPanel());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("前置脚本", FuDocIcons.FU_REQUEST_HEADER, this.rootPanel).builder();
    }

    @Override
    public void doAction(ScriptCmd scriptCmd) {
        if (!isEditor) {
            isEditor = true;
            switchRightPanel(this.fuEditorComponent.getMainPanel());
        }
        String cmd = scriptCmd.getCmd();
        String content = ResourceUtils.readResource("template/auth/" + cmd);
        if (scriptCmd.isReset()) {
            fuEditorComponent.setContent(content);
        } else {
            fuEditorComponent.append(content);
        }
    }




    /**
     * 切换面板
     *
     * @param panel 右侧展示的面板
     */
    private void switchRightPanel(JPanel panel) {
        this.leftPanel.removeAll();
        this.leftPanel.repaint();
        this.leftPanel.add(panel, BorderLayout.CENTER);
        this.leftPanel.revalidate();
    }

    /**
     * 初始化数据
     * @param data 数据对象
     */
    @Override
    public void initData(FuRequestConfigPO data) {

    }

    @Override
    public void saveData(FuRequestConfigPO data) {

    }
}
