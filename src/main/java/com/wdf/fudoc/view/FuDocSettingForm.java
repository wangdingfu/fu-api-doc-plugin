package com.wdf.fudoc.view;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.config.state.FuDocSetting;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.util.FastJsonUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-06 00:51:36
 */
@Getter
public class FuDocSettingForm {
    private JPanel root;
    private JTextArea settings;
    private JLabel title;
    private JTabbedPane tabPanel;
    private JTextArea fuDocTemplate;
    private JTextArea objectTemplate;
    private JTextArea enumTemplate1;
    private JTextArea enumTemplate2;
    private final Project project;
    private final SettingData settingData;

    public FuDocSettingForm(Project project) {
        this.project = project;
        this.settingData = FuDocSetting.getSettingData(this.project);
    }


    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
        String text = this.settings.getText();
        if (StringUtils.isNotBlank(text)) {
            this.settingData.setCustomerSettingData(JSONUtil.toBean(text, CustomerSettingData.class));
        }
        this.settingData.setFuDocTemplateValue(this.fuDocTemplate.getText());
        this.settingData.setObjectTemplateValue(this.objectTemplate.getText());
        this.settingData.setEnumTemplateValue1(this.enumTemplate1.getText());
        this.settingData.setEnumTemplateValue2(this.enumTemplate2.getText());
    }


    /**
     * 当进入页面或者点击reset按钮时 会调用该方法  重置设置页面内容
     */
    public void reset() {
        CustomerSettingData customerSettingData = this.settingData.getCustomerSettingData();
        if (Objects.nonNull(customerSettingData)) {
            this.settings.setText(FastJsonUtils.toJsonString(customerSettingData));
        }
        String fuDocTemplateValue = settingData.getFuDocTemplateValue();
        String objectTemplateValue = settingData.getObjectTemplateValue();
        String enumTemplateValue1 = settingData.getEnumTemplateValue1();
        String enumTemplateValue2 = settingData.getEnumTemplateValue2();
        if (StringUtils.isNotBlank(fuDocTemplateValue)) {
            this.fuDocTemplate.setText(fuDocTemplateValue);
        }
        if (StringUtils.isNotBlank(objectTemplateValue)) {
            this.objectTemplate.setText(objectTemplateValue);
        }
        if (StringUtils.isNotBlank(enumTemplateValue1)) {
            this.enumTemplate1.setText(enumTemplateValue1);
        }
        if (StringUtils.isNotBlank(enumTemplateValue2)) {
            this.enumTemplate2.setText(enumTemplateValue2);
        }
    }


}
