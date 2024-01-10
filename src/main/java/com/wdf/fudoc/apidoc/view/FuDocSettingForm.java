package com.wdf.fudoc.apidoc.view;

import cn.hutool.json.JSONUtil;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.apidoc.data.CustomerSettingData;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.util.FastJsonUtils;
import com.wdf.fudoc.components.FuEditorComponent;
import lombok.Getter;
import com.wdf.fudoc.util.FuStringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-06 00:51:36
 */
@Getter
public class FuDocSettingForm extends DialogWrapper {
    /**
     * 根节点面板
     */
    private JPanel root;


    /**
     * tab面板 用于挂载多个tab
     */
    private JTabbedPane tabPanel;

    /**
     * 当前项目的配置信息
     */
    private final SettingData settingData;

    /**
     * 接口文档模板面板
     */
    private JPanel mainTemplatePanel;
    /**
     * 对象接口文档模板面板
     */
    private JPanel objectTemplatePanel;

    /**
     * 枚举模板1面板
     */
    private JPanel enum1TemplatePanel;

    /**
     * 枚举模板2面板
     */
    private JPanel enum2TemplatePanel;

    /**
     * YApi模板
     */
    private JPanel yapiTemplatePanel;

    /**
     * 配置信息面板
     */
    private JPanel settingPanel;


    /**
     * 对应的编辑器组件
     */
    private FuEditorComponent mainEditorComponent;
    private FuEditorComponent objectEditorComponent;
    private FuEditorComponent enum1EditorComponent;
    private FuEditorComponent enum2EditorComponent;
    private FuEditorComponent settingEditorComponent;
    private FuEditorComponent yapiEditorComponent;

    public FuDocSettingForm(Project project, SettingData settingData) {
        super(project, true, IdeModalityType.PROJECT);
        this.settingData = settingData;
    }

    @Override
    public @Nullable JComponent createCenterPanel() {
        return this.root;
    }

    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
        String content = this.settingEditorComponent.getContent();
        if (FuStringUtils.isNotBlank(content)) {
            this.settingData.setCustomerSettingData(JSONUtil.toBean(content, CustomerSettingData.class));
        }
        this.settingData.setFuDocTemplateValue(this.mainEditorComponent.getContent());
        this.settingData.setObjectTemplateValue(this.objectEditorComponent.getContent());
        this.settingData.setEnumTemplateValue1(this.enum1EditorComponent.getContent());
        this.settingData.setEnumTemplateValue2(this.enum2EditorComponent.getContent());
        this.settingData.setYapiTemplateValue(this.yapiEditorComponent.getContent());
    }


    /**
     * 当进入页面或者点击reset按钮时 会调用该方法  重置设置页面内容
     */
    public void reset() {
        CustomerSettingData customerSettingData = this.settingData.getCustomerSettingData();
        if (Objects.nonNull(customerSettingData)) {
            this.settingEditorComponent.setContent(FastJsonUtils.toJsonString(customerSettingData));
        }
        String fuDocTemplateValue = settingData.getFuDocTemplateValue();
        String objectTemplateValue = settingData.getObjectTemplateValue();
        String enumTemplateValue1 = settingData.getEnumTemplateValue1();
        String enumTemplateValue2 = settingData.getEnumTemplateValue2();
        String yapiTemplateValue = settingData.getYapiTemplateValue();
        if (FuStringUtils.isNotBlank(fuDocTemplateValue)) {
            this.mainEditorComponent.setContent(fuDocTemplateValue);
        }
        if (FuStringUtils.isNotBlank(objectTemplateValue)) {
            this.objectEditorComponent.setContent(objectTemplateValue);
        }
        if (FuStringUtils.isNotBlank(enumTemplateValue1)) {
            this.enum1EditorComponent.setContent(enumTemplateValue1);
        }
        if (FuStringUtils.isNotBlank(enumTemplateValue2)) {
            this.enum2EditorComponent.setContent(enumTemplateValue2);
        }
        if (FuStringUtils.isNotBlank(yapiTemplateValue)) {
            this.yapiEditorComponent.setContent(yapiTemplateValue);
        }
    }


    private void createUIComponents() {
        Disposable disposable = getDisposable();
        //初始化编辑器组件
        this.mainEditorComponent = FuEditorComponent.create(XmlFileType.INSTANCE, null,disposable);
        this.objectEditorComponent = FuEditorComponent.create(XmlFileType.INSTANCE, null,disposable);
        this.enum1EditorComponent = FuEditorComponent.create(XmlFileType.INSTANCE, null,disposable);
        this.enum2EditorComponent = FuEditorComponent.create(XmlFileType.INSTANCE, null,disposable);
        this.settingEditorComponent = FuEditorComponent.create(JsonFileType.INSTANCE, null,disposable);
        this.yapiEditorComponent = FuEditorComponent.create(XmlFileType.INSTANCE, null,disposable);

        //初始化面板
        this.mainTemplatePanel = new JPanel(new BorderLayout());
        this.objectTemplatePanel = new JPanel(new BorderLayout());
        this.enum1TemplatePanel = new JPanel(new BorderLayout());
        this.enum2TemplatePanel = new JPanel(new BorderLayout());
        this.yapiTemplatePanel = new JPanel(new BorderLayout());
        this.settingPanel = new JPanel(new BorderLayout());

        //将编辑器组件挂载到面板上
        this.mainTemplatePanel.add(this.mainEditorComponent.getMainPanel());
        this.objectTemplatePanel.add(this.objectEditorComponent.getMainPanel());
        this.enum1TemplatePanel.add(this.enum1EditorComponent.getMainPanel());
        this.enum2TemplatePanel.add(this.enum2EditorComponent.getMainPanel());
        this.yapiTemplatePanel.add(this.yapiEditorComponent.getMainPanel());
        this.settingPanel.add(this.settingEditorComponent.getMainPanel());
    }

}
