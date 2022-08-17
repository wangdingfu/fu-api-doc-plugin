package com.wdf.fudoc.view;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.util.FastJsonUtils;
import com.wdf.fudoc.view.components.FuEditorComponent;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-06 00:51:36
 */
@Getter
public class FuDocSettingForm {
    /**
     * 根节点面板
     */
    private JPanel root;


    private JLabel title;

    /**
     * tab面板 用于挂载多个tab
     */
    private JTabbedPane tabPanel;


    /**
     * 当前项目
     */
    private final Project project;

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

    public FuDocSettingForm(Project project, SettingData settingData) {
        this.project = project;
        this.settingData = settingData;
    }


    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
//        String text = this.settings.getText();
//        if (StringUtils.isNotBlank(text)) {
//            this.settingData.setCustomerSettingData(JSONUtil.toBean(text, CustomerSettingData.class));
//        }
//        this.settingData.setFuDocTemplateValue();
//        this.settingData.setObjectTemplateValue(this.objectTemplate.getText());
//        this.settingData.setEnumTemplateValue1(this.enumTemplate1.getText());
//        this.settingData.setEnumTemplateValue2(this.enumTemplate2.getText());
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
        if (StringUtils.isNotBlank(fuDocTemplateValue)) {
            this.mainEditorComponent.setContent(fuDocTemplateValue);
        }
        if (StringUtils.isNotBlank(objectTemplateValue)) {
            this.objectEditorComponent.setContent(objectTemplateValue);
        }
        if (StringUtils.isNotBlank(enumTemplateValue1)) {
            this.enum1EditorComponent.setContent(enumTemplateValue1);
        }
        if (StringUtils.isNotBlank(enumTemplateValue2)) {
            this.enum2EditorComponent.setContent(enumTemplateValue2);
        }
    }


    private void createUIComponents() {
        //初始化编辑器组件
        this.mainEditorComponent = FuEditorComponent.create(this.project, null);
        this.objectEditorComponent = FuEditorComponent.create(this.project, null);
        this.enum1EditorComponent = FuEditorComponent.create(this.project, null);
        this.enum2EditorComponent = FuEditorComponent.create(this.project, null);
        this.settingEditorComponent = FuEditorComponent.create(this.project, null);

        //初始化面板
        this.mainTemplatePanel = new JPanel(new BorderLayout());
        this.objectTemplatePanel = new JPanel(new BorderLayout());
        this.enum1TemplatePanel = new JPanel(new BorderLayout());
        this.enum2TemplatePanel = new JPanel(new BorderLayout());
        this.settingPanel = new JPanel(new BorderLayout());

        //将编辑器组件挂载到面板上
        this.mainTemplatePanel.add(this.mainEditorComponent.getMainPanel());
        this.objectTemplatePanel.add(this.objectEditorComponent.getMainPanel());
        this.enum1TemplatePanel.add(this.enum1EditorComponent.getMainPanel());
        this.enum2TemplatePanel.add(this.enum2EditorComponent.getMainPanel());
        this.settingPanel.add(this.settingEditorComponent.getMainPanel());
    }
}
