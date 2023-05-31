package com.wdf.fudoc.request.tab.auth;

import com.google.common.collect.Lists;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.TipCmdComponent;
import com.wdf.fudoc.components.bo.TipCmd;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.JavaCodeAuthConfig;
import com.wdf.fudoc.util.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:45:44
 */
public class JavaScriptCodeAuthTab implements FuTab, FuActionListener<AuthConfigData> {

    private final JPanel rootPanel;
    private final JComponent codeTipPanel;
    private final FuEditorComponent fuEditorComponent;

    public final static String TAB = "JavaScript";

    private static final List<TipCmd> cmdList = Lists.newArrayList(
            new TipCmd("设置当前项目参数","fu.projectEnv.set('','')"),
            new TipCmd("设置全局请求头","fu.env.set('','')"),
            new TipCmd("获取响应结果","fu.result.data")
    );

    public JavaScriptCodeAuthTab() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.codeTipPanel = new JPanel(new BorderLayout());
        this.codeTipPanel.add(new TipCmdComponent(cmdList).getList(), BorderLayout.CENTER);
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE);
        Splitter splitter = new Splitter(false, 0.7F);
        splitter.setFirstComponent(this.fuEditorComponent.getMainPanel());
        splitter.setSecondComponent(this.codeTipPanel);
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TAB, null, this.rootPanel).builder();
    }


    @Override
    public void doAction(AuthConfigData data) {
        JavaCodeAuthConfig authConfig = (JavaCodeAuthConfig) data.getAuthConfig(TAB);
        if (Objects.isNull(authConfig)) {
            authConfig = new JavaCodeAuthConfig();
            authConfig.setJavaCode(ResourceUtils.readResource("/template/auth_config.js"));
        }
        this.fuEditorComponent.setContent(authConfig.getJavaCode());
    }

    @Override
    public void doActionAfter(AuthConfigData data) {
        JavaCodeAuthConfig authConfig = (JavaCodeAuthConfig) data.getAuthConfig(TAB);
        if (Objects.isNull(authConfig)) {
            authConfig = new JavaCodeAuthConfig();
        }
        authConfig.setJavaCode(this.fuEditorComponent.getContent());
    }
}
