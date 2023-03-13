package com.wdf.fudoc.request.tab.auth;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;

/**
 * @author wangdingfu
 * @date 2023-03-12 17:45:44
 */
public class JavaScriptCodeAuthTab  implements FuTab, FuActionListener<AuthConfigData> {
    private final FuEditorComponent fuEditorComponent;

    public final static String TAB = "JAVA_SCRIPT";

    public JavaScriptCodeAuthTab() {
        this.fuEditorComponent = FuEditorComponent.create(JavaFileType.INSTANCE);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TAB, null, this.fuEditorComponent.getMainPanel()).builder();
    }

    @Override
    public void doAction(AuthConfigData data) {

    }

    @Override
    public void doActionAfter(AuthConfigData data) {

    }
}
