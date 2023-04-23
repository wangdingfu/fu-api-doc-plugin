package com.wdf.fudoc.request.tab.auth;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.JavaCodeAuthConfig;
import com.wdf.fudoc.util.ResourceUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:45:44
 */
public class JavaScriptCodeAuthTab  implements FuTab, FuActionListener<AuthConfigData> {
    private final FuEditorComponent fuEditorComponent;

    public final static String TAB = "JavaScript";

    public JavaScriptCodeAuthTab() {
        this.fuEditorComponent = FuEditorComponent.create(JavaScriptFileType.INSTANCE);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TAB, null, this.fuEditorComponent.getMainPanel()).builder();
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
