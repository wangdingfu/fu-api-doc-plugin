package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.EditorListComponent;
import com.wdf.fudoc.components.LeftRightComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * 鉴权设置页面
 *
 * @author wangdingfu
 * @date 2023-02-16 21:55:28
 */
public class AuthSettingView {


    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;

    /**
     * 左侧列表组件 维护多个鉴权
     */
    private final EditorListComponent<AuthConfigData> leftComponent;

    /**
     * 右侧权限配置页面
     */
    private final AuthConfigView authConfigView;


    public AuthSettingView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.authConfigView = new AuthConfigView(project);
        this.leftComponent = new EditorListComponent<>(new AuthConfigListener(), "权限名称", Lists.newArrayList(), AuthConfigData.class);
        this.rootPanel.add(new LeftRightComponent(this.leftComponent.getRootPanel(), this.authConfigView.getRootPanel()).getRootPanel(), BorderLayout.CENTER);
    }


    /**
     * 左侧列表选中时触发事件 初始化右侧面板
     */
    class AuthConfigListener implements FuActionListener<AuthConfigData> {

        @Override
        public void doAction(AuthConfigData data) {
            authConfigView.init(data);
        }
    }
}
