package com.wdf.fudoc.request.tab.settings;

import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuCmdComponent;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.request.view.ModuleScopeView;
import com.wdf.fudoc.util.ResourceUtils;
import com.wdf.fudoc.util.ToolBarUtils;
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

    private final JPanel rightPanel;

    private final JPanel emptyPanel;

    private final JPanel scopePanel;

    private final FuEditorComponent fuEditorComponent;

    private final ModuleScopeView moduleScopeView;


    private boolean isEditor = false;

    public GlobalPreScriptTab(Project project) {
        this.project = project;
        this.rootPanel = new JPanel(new BorderLayout());
        this.emptyPanel = FuEditorEmptyTextPainter.createFramePreview();
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE);
        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(this.emptyPanel, BorderLayout.CENTER);
        this.moduleScopeView = new ModuleScopeView(project);
        this.scopePanel = createScopePanel();
        this.rightPanel = new JPanel(new BorderLayout());
        this.rightPanel.add(createRightPanel(), BorderLayout.CENTER);
        Splitter splitter = new Splitter(false, 0.7F);
        splitter.setFirstComponent(this.leftPanel);
        splitter.setSecondComponent(this.rightPanel);
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }

    private JPanel createScopePanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("作用范围"), BorderLayout.WEST);
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("设置作用范围", "Setting", AllIcons.General.Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                //展示设置界面
                if (moduleScopeView.showAndGet()) {
                    //重新渲染
                    switchPanel(rightPanel, createRightPanel());
                }
            }
        });
        ToolBarUtils.addActionToToolBar(panel, RequestConstants.PLACE_SETTINGS_ADD_TAB, actionGroup, BorderLayout.EAST);
        return panel;
    }


    private JPanel createRightPanel() {
        FuCmdComponent fuCmdComponent = FuCmdComponent.getInstance(this);
        fuCmdComponent.addComponent(this.scopePanel);
        for (String module : this.moduleScopeView.getSelected()) {
            JLabel jLabel = new JLabel(module, AllIcons.Nodes.Module, SwingConstants.LEFT);
            jLabel.setBorder(JBUI.Borders.empty(3, 10, 3, 0));
            fuCmdComponent.addComponent(jLabel);
        }
        fuCmdComponent.addStrut(20);
        ScriptCmd.execute((cmdType, list) -> fuCmdComponent.addCmd(cmdType.getDesc(), list));
        return fuCmdComponent.getRootPanel();
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("前置脚本", FuDocIcons.FU_REQUEST_HEADER, this.rootPanel).builder();
    }

    @Override
    public void doAction(ScriptCmd scriptCmd) {
        if (!isEditor) {
            isEditor = true;
            switchPanel(this.leftPanel, this.fuEditorComponent.getMainPanel());
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
    private void switchPanel(JPanel panel, JPanel switchPanel) {
        panel.removeAll();
        panel.repaint();
        panel.add(switchPanel, BorderLayout.CENTER);
        panel.revalidate();
    }

    /**
     * 初始化数据
     *
     * @param data 数据对象
     */
    @Override
    public void initData(FuRequestConfigPO data) {

    }

    @Override
    public void saveData(FuRequestConfigPO data) {

    }
}
