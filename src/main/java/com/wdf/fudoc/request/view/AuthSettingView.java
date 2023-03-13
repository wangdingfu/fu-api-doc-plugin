package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuListStringComponent;
import com.wdf.fudoc.components.LeftRightComponent;
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
    private final FuListStringComponent<AuthConfigData> leftComponent;

    /**
     * 右侧权限配置页面
     */
    private final AuthConfigView authConfigView;


    public AuthSettingView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.authConfigView = new AuthConfigView(project);
        this.leftComponent = new FuListStringComponent<>("权限名称", this.authConfigView, AuthConfigData.class);
        this.rootPanel.add(new LeftRightComponent(this.leftComponent.createPanel(), this.authConfigView.getRootPanel()).getRootPanel(), BorderLayout.CENTER);
    }


}
