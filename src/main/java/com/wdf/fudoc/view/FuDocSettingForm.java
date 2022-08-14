package com.wdf.fudoc.view;

import com.alibaba.fastjson.JSON;
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

    public FuDocSettingForm(Project project) {
        this.project = project;
    }


    public void apply() {
        FuDocSetting fuDocSetting = FuDocSetting.getInstance(this.project);
        SettingData settingData = fuDocSetting.getSettingData();
        String text = this.settings.getText();
        if (StringUtils.isNotBlank(text)) {
            settingData.setCustomerSettingData(JSON.parseObject(text, CustomerSettingData.class));
        }
        settingData.setFuDocTemplateValue(this.fuDocTemplate.getText());
        settingData.setObjectTemplateValue(this.objectTemplate.getText());
        settingData.setEnumTemplateValue1(this.enumTemplate1.getText());
        settingData.setEnumTemplateValue2(this.enumTemplate2.getText());
    }


    public void reset() {
        FuDocSetting fuDocSetting = FuDocSetting.getInstance(project);
        SettingData settingData = fuDocSetting.getSettingData();
        if (Objects.nonNull(settingData)) {
            CustomerSettingData customerSettingData = settingData.getCustomerSettingData();
            if(Objects.nonNull(customerSettingData)){
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


}
