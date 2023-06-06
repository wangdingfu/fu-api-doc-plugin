package com.wdf.fudoc.request.view;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.FuPaths;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuListStringComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.LeftRightComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.BaseAuthConfig;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.storage.service.AuthConfigStorageService;
import com.wdf.fudoc.util.StorageUtils;
import icons.FuDocIcons;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.getIdeBackgroundColor;

/**
 * 鉴权设置页面
 *
 * @author wangdingfu
 * @date 2023-02-16 21:55:28
 */
public class AuthSettingView implements FuTab, FuActionListener<AuthConfigData> {

    private static final String TITLE = "鉴权配置";

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;


    private final JComponent rightPanel;

    private final JPanel emptyPanel;


    /**
     * 左侧列表组件 维护多个鉴权
     */
    private final FuListStringComponent<AuthConfigData> leftComponent;

    /**
     * 右侧权限配置页面
     */
    private final AuthConfigView authConfigView;

    private final AtomicBoolean init = new AtomicBoolean(true);


    public AuthSettingView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.authConfigView = new AuthConfigView(project);
        this.rightPanel = new JPanel(new BorderLayout());
        this.emptyPanel = createFramePreview();
        this.rightPanel.add(this.emptyPanel, BorderLayout.CENTER);
        this.leftComponent = new FuListStringComponent<>("权限名称", this, AuthConfigData.class);
        this.rootPanel.add(new LeftRightComponent(this.leftComponent.createPanel(), this.rightPanel).getRootPanel(), BorderLayout.CENTER);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TITLE, FuDocIcons.FU_AUTH, this.rootPanel).builder();
    }

    @Override
    public void doAction(AuthConfigData data) {
        if (init.get()) {
            init.set(false);
            this.rightPanel.removeAll();
            this.rightPanel.repaint();
            this.rightPanel.add(this.authConfigView.getRootPanel(), BorderLayout.CENTER);
            this.rightPanel.revalidate();
        }
        this.authConfigView.doAction(data);
    }

    @Override
    public void remove(AuthConfigData data) {
        if (this.leftComponent.getSize() == 0) {
            init.set(true);
            this.rightPanel.removeAll();
            this.rightPanel.repaint();
            this.rightPanel.add(this.emptyPanel, BorderLayout.CENTER);
            this.rightPanel.revalidate();
        }
    }

    @Override
    public void doActionAfter(AuthConfigData data) {
        this.authConfigView.doActionAfter(data);
    }

    public void reset() {
        //读取配置内容
        String content = StorageUtils.readContent(FuPaths.AUTH_PATH, FuPaths.PACKAGE);
        if (StringUtils.isBlank(content)) {
            return;
        }
        List<String> nameList = JSONUtil.parseArray(content).toList(String.class);
        for (String name : nameList) {
            this.leftComponent.addRow(AuthConfigStorageService.read(name));
        }
    }


    /**
     * 将配置数据持久化到硬盘中
     */
    public void apply() {
        List<String> nameList = this.leftComponent.getNameList();
        Map<String, AuthConfigData> configData = this.leftComponent.getDataMap();
        if (CollectionUtils.isEmpty(nameList)) {
            return;
        }
        //持久化名字集合
        StorageUtils.writeJson(FuPaths.AUTH_PATH, FuPaths.PACKAGE, nameList);
        for (String name : nameList) {
            if (StringUtils.isBlank(name)) {
                continue;
            }
            AuthConfigData authConfigData = configData.get(name);
            if (Objects.isNull(authConfigData)) {
                continue;
            }
            //持久化鉴权配置
            AuthConfigStorageService.write(authConfigData);
        }
    }


    private static JPanel createFramePreview() {
        FuEditorEmptyTextPainter painter = new FuEditorEmptyTextPainter();
        JBPanelWithEmptyText panel = new JBPanelWithEmptyText() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                painter.paintEmptyText(this, g);
            }

            @Override
            public Color getBackground() {
                return getIdeBackgroundColor();
            }

            @Override
            public boolean isOpaque() {
                return true;
            }
        };
        panel.getEmptyText().clear();
        return panel;
    }

}
