package com.wdf.fudoc.request.tab.auth;

import com.intellij.icons.AllIcons;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuCmdComponent;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.JavaCodeAuthConfig;
import com.wdf.fudoc.request.pojo.ScriptConfigData;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:45:44
 */
public class JavaScriptCodeAuthTab implements FuTab, FuActionListener<AuthConfigData> {

    private final JPanel rootPanel;
    private final FuEditorComponent fuEditorComponent;

    public final static String TAB = "script";


    public JavaScriptCodeAuthTab() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE,this);
        FuCmdComponent instance = FuCmdComponent.getInstance(null);
        ScriptCmd.execute((cmdType, list) -> instance.addCmd(cmdType.getDesc(), list, null));
        Splitter splitter = new Splitter(false, 0.7F);
        splitter.setFirstComponent(this.fuEditorComponent.getMainPanel());
        splitter.setSecondComponent(instance.getVerticalBox());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TAB, AllIcons.FileTypes.JavaScript, this.rootPanel).builder();
    }


    @Override
    public void doAction(AuthConfigData data) {
        ScriptConfigData authConfig = (ScriptConfigData) data.getAuthConfig(TAB);
        if (Objects.isNull(authConfig)) {
            authConfig = new ScriptConfigData();
            authConfig.setScript(StringUtils.EMPTY);
            data.addAuthConfig(TAB, authConfig);
        }
        this.fuEditorComponent.setContent(authConfig.getScript());
    }

    @Override
    public void doActionAfter(AuthConfigData data) {
        ScriptConfigData authConfig = (ScriptConfigData) data.getAuthConfig(TAB);
        if (Objects.isNull(authConfig)) {
            authConfig = new ScriptConfigData();
            data.addAuthConfig(TAB, authConfig);
        }
        authConfig.setScript(this.fuEditorComponent.getContent());
    }

    @Override
    public void dispose() {

    }
}
