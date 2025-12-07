package com.wdf.fudoc.request.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.util.FuStringUtils;
import icons.FuDocIcons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 环境快速配置对话框
 *
 * @author wangdingfu
 * @date 2023-12-05 10:00:00
 */
@Slf4j
public class EnvQuickConfigDialog extends DialogWrapper {

    private final Project project;
    private final List<ConfigEnvTableBO> envList;

    private JBTextField envNameField;
    private JBTextField envTypeField;
    private JBTextField domainField;
    private JBTextField portField;
    private JBTextField contextPathField;
    private JBTextField descField;
    private JComboBox<String> envTemplateCombo;

    public EnvQuickConfigDialog(@NotNull Project project, List<ConfigEnvTableBO> envList) {
        super(project);
        this.project = project;
        this.envList = envList != null ? envList : Lists.newArrayList();
        init();
        setTitle("环境快速配置");
        setModal(true);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        // 创建模板选择
        envTemplateCombo = new JComboBox<>(new String[]{"自定义", "开发环境", "测试环境", "预发布环境", "生产环境"});
        envTemplateCombo.addActionListener(e -> applyTemplate());

        // 创建表单字段
        envNameField = new JBTextField();
        envTypeField = new JBTextField();
        domainField = new JBTextField();
        portField = new JBTextField("8080");
        contextPathField = new JBTextField();
        descField = new JBTextField();

        // 构建表单
        FormBuilder formBuilder = FormBuilder.createFormBuilder()
                .addComponent(new JBLabel("环境模板"), 1)
                .addComponent(envTemplateCombo, 1)
                .addVerticalGap(10)
                .addComponent(new JBLabel("环境名称 *"), 1)
                .addComponent(envNameField, 1)
                .addComponent(new JBLabel("环境类型"), 1)
                .addComponent(envTypeField, 1)
                .addComponent(new JBLabel("域名 *"), 1)
                .addComponent(domainField, 1)
                .addComponent(new JBLabel("端口"), 1)
                .addComponent(portField, 1)
                .addComponent(new JBLabel("上下文路径"), 1)
                .addComponent(contextPathField, 1)
                .addComponent(new JBLabel("环境描述"), 1)
                .addComponent(descField, 1);

        JPanel panel = formBuilder.getPanel();
        panel.setPreferredSize(JBUI.size(500, 350));

        return panel;
    }

    /**
     * 应用环境模板
     */
    private void applyTemplate() {
        int selectedIndex = envTemplateCombo.getSelectedIndex();
        switch (selectedIndex) {
            case 1: // 开发环境
                envNameField.setText("dev");
                envTypeField.setText("dev");
                domainField.setText("localhost");
                portField.setText("8080");
                contextPathField.setText("");
                descField.setText("开发环境");
                break;
            case 2: // 测试环境
                envNameField.setText("test");
                envTypeField.setText("test");
                domainField.setText("test.example.com");
                portField.setText("80");
                contextPathField.setText("");
                descField.setText("测试环境");
                break;
            case 3: // 预发布环境
                envNameField.setText("pre");
                envTypeField.setText("pre");
                domainField.setText("pre.example.com");
                portField.setText("80");
                contextPathField.setText("");
                descField.setText("预发布环境");
                break;
            case 4: // 生产环境
                envNameField.setText("prod");
                envTypeField.setText("prod");
                domainField.setText("prod.example.com");
                portField.setText("80");
                contextPathField.setText("");
                descField.setText("生产环境");
                break;
            default: // 自定义
                envNameField.setText("");
                envTypeField.setText("");
                domainField.setText("");
                portField.setText("8080");
                contextPathField.setText("");
                descField.setText("");
                break;
        }
    }

    @Override
    protected @NotNull Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if (FuStringUtils.isBlank(envNameField.getText())) {
            return new ValidationInfo("环境名称不能为空", envNameField);
        }

        if (FuStringUtils.isBlank(domainField.getText())) {
            return new ValidationInfo("域名不能为空", domainField);
        }

        // 检查环境名称是否重复
        String envName = envNameField.getText().trim();
        boolean isDuplicate = envList.stream()
                .anyMatch(env -> envName.equals(env.getEnvName()));

        if (isDuplicate) {
            return new ValidationInfo("环境名称已存在", envNameField);
        }

        try {
            String portText = portField.getText().trim();
            if (FuStringUtils.isNotBlank(portText)) {
                int port = Integer.parseInt(portText);
                if (port < 1 || port > 65535) {
                    return new ValidationInfo("端口必须在1-65535之间", portField);
                }
            }
        } catch (NumberFormatException e) {
            return new ValidationInfo("端口号格式错误", portField);
        }

        return null;
    }

    public ConfigEnvTableBO getEnvConfig() {
        ConfigEnvTableBO envConfig = new ConfigEnvTableBO();
        envConfig.setEnvName(envNameField.getText().trim());
        envConfig.setEnvType(envTypeField.getText().trim());
        envConfig.setDomain(domainField.getText().trim());

        String portText = portField.getText().trim();
        if (FuStringUtils.isNotBlank(portText)) {
            envConfig.setPort(Integer.parseInt(portText));
        }

        String contextPath = contextPathField.getText().trim();
        if (FuStringUtils.isNotBlank(contextPath)) {
            envConfig.setContextPath(contextPath);
        }

        envConfig.setDescription(descField.getText().trim());
        envConfig.setSelect(true);
        envConfig.setEnabled(true);

        return envConfig;
    }

    /**
     * 显示环境配置对话框
     *
     * @param project 项目
     * @param envList 现有环境列表
     * @return 配置的环境，如果取消则返回null
     */
    public static ConfigEnvTableBO showEnvConfigDialog(Project project, List<ConfigEnvTableBO> envList) {
        EnvQuickConfigDialog dialog = new EnvQuickConfigDialog(project, envList);
        if (dialog.showAndGet()) {
            return dialog.getEnvConfig();
        }
        return null;
    }
}