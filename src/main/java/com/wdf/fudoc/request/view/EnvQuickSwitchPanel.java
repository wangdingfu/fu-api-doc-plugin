package com.wdf.fudoc.request.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.dialog.EnvQuickConfigDialog;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.spring.SpringBootEnvModuleInfo;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 环境快速切换面板
 *
 * @author wangdingfu
 * @date 2023-12-05 15:00:00
 */
public class EnvQuickSwitchPanel extends JBPanel<EnvQuickSwitchPanel> {

    private final Project project;
    private final RequestTabView requestTabView;
    private final FuRequestCallback fuRequestCallback;

    private ComboBox<EnvItem> envComboBox;
    private JButton addButton;
    private JButton refreshButton;
    private JLabel statusLabel;

    public EnvQuickSwitchPanel(Project project, RequestTabView requestTabView, FuRequestCallback fuRequestCallback) {
        this.project = project;
        this.requestTabView = requestTabView;
        this.fuRequestCallback = fuRequestCallback;
        initUI();
        refreshEnvs();
    }

    private void initUI() {
        // 创建组件
        envComboBox = new ComboBox<>();
        envComboBox.setPrototypeDisplayValue(new EnvItem("测试环境", "test", null));
        envComboBox.setToolTipText("选择要使用的环境配置");

        addButton = new JButton(AllIcons.General.Add);
        addButton.setToolTipText("添加新环境");
        addButton.addActionListener(e -> addNewEnv());

        refreshButton = new JButton(AllIcons.Actions.Refresh);
        refreshButton.setToolTipText("刷新环境列表");
        refreshButton.addActionListener(e -> refreshEnvs());

        statusLabel = new JBLabel();
        statusLabel.setIcon(FuDocIcons.SPRING_BOOT);

        // 监听选择变化
        envComboBox.addActionListener(e -> {
            EnvItem selected = (EnvItem) envComboBox.getSelectedItem();
            if (selected != null && selected.getConfig() != null) {
                switchToEnv(selected.getConfig());
            }
        });

        // 布局
        FormBuilder builder = FormBuilder.createFormBuilder()
                .addComponent(createLeftPanel(), 1)
                .addComponent(statusLabel, 1);

        setLayout(new BorderLayout());
        add(builder.getPanel(), BorderLayout.CENTER);

        // 设置边距
        setBorder(JBUI.Borders.empty(5));
    }

    private JPanel createLeftPanel() {
        JBPanel panel = new JBPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JBLabel("环境:"));
        panel.add(envComboBox);
        panel.add(addButton);
        panel.add(refreshButton);
        return panel;
    }

    /**
     * 刷新环境列表
     */
    public void refreshEnvs() {
        FuRequestConfigPO configPO = FuRequestConfigStorage.get(project).readData();
        List<ConfigEnvTableBO> envConfigList = configPO.getEnvConfigList();

        if (CollectionUtils.isEmpty(envConfigList)) {
            envComboBox.removeAllItems();
            envComboBox.addItem(new EnvItem("暂无环境", null, null));
            envComboBox.setEnabled(false);
            statusLabel.setText("未配置环境");
            return;
        }

        Module module = getCurrentModule();
        if (module == null) {
            envComboBox.removeAllItems();
            envComboBox.addItem(new EnvItem("请选择模块", null, null));
            envComboBox.setEnabled(false);
            statusLabel.setText("无当前模块");
            return;
        }

        // 过滤当前模块的环境（合并为单次流操作，提升性能）
        SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = SpringBootEnvLoader.getEnvInfo(module);
        String applicationName = envInfo != null ? envInfo.getApplicationName() : null;

        List<ConfigEnvTableBO> filteredEnvs = envConfigList.stream()
                .filter(env -> isValidEnv(env, applicationName))
                .collect(Collectors.toList());

        envComboBox.removeAllItems();
        envComboBox.setEnabled(true);

        if (filteredEnvs.isEmpty()) {
            envComboBox.addItem(new EnvItem("无可用环境", null, null));
            statusLabel.setText("当前模块无环境配置");
        } else {
            // 添加环境选项
            for (ConfigEnvTableBO env : filteredEnvs) {
                String displayText = env.getEnvName();
                if (FuStringUtils.isNotBlank(env.getEnvType())) {
                    displayText += " (" + env.getEnvType() + ")";
                }
                envComboBox.addItem(new EnvItem(displayText, env.getEnvName(), env));
            }

            // 设置当前选中的环境
            String currentEnv = configPO.getEnv(module.getName());
            if (FuStringUtils.isNotBlank(currentEnv)) {
                for (int i = 0; i < envComboBox.getItemCount(); i++) {
                    EnvItem item = envComboBox.getItemAt(i);
                    if (item != null && currentEnv.equals(item.getEnvName())) {
                        envComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }

            statusLabel.setText("共 " + filteredEnvs.size() + " 个环境");
        }
    }

    /**
     * 添加新环境
     */
    private void addNewEnv() {
        FuRequestConfigPO configPO = FuRequestConfigStorage.get(project).readData();
        ConfigEnvTableBO newEnv = EnvQuickConfigDialog.showEnvConfigDialog(project, configPO.getEnvConfigList());

        if (newEnv != null) {
            Module module = getCurrentModule();
            if (module != null) {
                SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = SpringBootEnvLoader.getEnvInfo(module);
                if (envInfo != null) {
                    newEnv.setApplication(envInfo.getApplicationName());
                }
            }
            configPO.getEnvConfigList().add(newEnv);
            FuRequestConfigStorage.get(project).saveData(configPO);
            refreshEnvs();
        }
    }

    /**
     * 切换到指定环境
     */
    private void switchToEnv(ConfigEnvTableBO env) {
        Module module = getCurrentModule();
        if (module == null) {
            return;
        }

        // 更新配置
        FuRequestConfigPO configPO = FuRequestConfigStorage.get(project).readData();
        configPO.addDefaultEnv(module.getName(), env.getEnvName());
        FuRequestConfigStorage.get(project).saveData(configPO);

        // 更新请求
        FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
        if (fuHttpRequestData != null) {
            FuRequestData request = fuHttpRequestData.getRequest();
            if (request != null) {
                // 使用工具类构建完整 URL
                String domain = com.wdf.fudoc.util.UrlBuildUtils.buildFullUrl(env.getDomain(), env.getPort());
                request.setDomain(domain);

                // 设置上下文路径（规范化处理）
                request.setContextPath(com.wdf.fudoc.util.UrlBuildUtils.normalizeContextPath(env.getContextPath()));

                request.setRequestUrl(null);
                requestTabView.setRequestUrl(request.getRequestUrl());
            }
        }
    }

    /**
     * 获取当前模块
     */
    private Module getCurrentModule() {
        FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
        if (fuHttpRequestData != null) {
            return fuHttpRequestData.getModule();
        }
        return null;
    }

    /**
     * 判断环境配置是否有效（合并多个过滤条件，减少流遍历次数）
     *
     * @param env             环境配置
     * @param applicationName 当前应用名称
     * @return 是否有效
     */
    private boolean isValidEnv(ConfigEnvTableBO env, String applicationName) {
        // 基础字段校验
        if (FuStringUtils.isBlank(env.getApplication()) ||
            FuStringUtils.isBlank(env.getDomain()) ||
            FuStringUtils.isBlank(env.getEnvName())) {
            return false;
        }

        // 选中状态校验
        if (!env.isSelect()) {
            return false;
        }

        // 启用状态校验
        if (env.getEnabled() != null && !env.getEnabled()) {
            return false;
        }

        // 应用名称匹配校验
        if (applicationName != null && !applicationName.equals(env.getApplication())) {
            return false;
        }

        return true;
    }

    /**
     * 环境选项类
     */
    private static class EnvItem {
        private final String displayText;
        private final String envName;
        private final ConfigEnvTableBO config;

        public EnvItem(String displayText, String envName, ConfigEnvTableBO config) {
            this.displayText = displayText;
            this.envName = envName;
            this.config = config;
        }

        public String getEnvName() {
            return envName;
        }

        public ConfigEnvTableBO getConfig() {
            return config;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }
}