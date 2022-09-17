package com.wdf.fudoc.apidoc.view;

import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.data.CustomerSettingData;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.apidoc.data.SettingDynamicValueData;
import com.wdf.fudoc.request.factory.FuTableColumnFactory;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.components.FuTableComponent;
import lombok.Getter;

import javax.swing.*;

/**
 * Fu Doc 基础设置页面
 *
 * @author wangdingfu
 * @date 2022-08-14 21:31:11
 */
public class FuDocGeneralForm {

    private SettingData settingData;

    /**
     * 自定义持久化数据
     */
    private CustomerSettingData customerSettingData;

    /**
     * 根面板(当前配置页面所有的东西都会挂在这个根面板下)
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 过滤属性配置面板
     */
    private JPanel filterPanel;

    /**
     * 自定义数据配置面板
     */
    private JPanel customDataPanel;

    private FuTableComponent<FilterFieldBO> filterFuTableComponent;
    private FuTableComponent<SettingDynamicValueData> customDataFuTableComponent;


    public FuDocGeneralForm() {
    }

    /**
     * 系统会自动调用该方法来完成自定义panel的创建
     */
    private void createUIComponents() {
        this.settingData = FuDocSetting.getSettingData();
        //持久化数据对象
        this.customerSettingData = this.settingData.getCustomerSettingData();
        //创建过滤属性面板
        this.filterFuTableComponent = FuTableComponent.create(FuTableColumnFactory.filterColumns(), customerSettingData.getSettings_filter_field(), FilterFieldBO.class);
        this.filterPanel = this.filterFuTableComponent.createPanel();

        //创建自定义数据面板
        this.customDataFuTableComponent = FuTableComponent.create(FuTableColumnFactory.dynamicColumns(), customerSettingData.getSetting_customer_value(), SettingDynamicValueData.class);
        this.customDataPanel = this.customDataFuTableComponent.createPanel();
    }


    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
        this.customerSettingData.setSettings_filter_field(this.filterFuTableComponent.getDataList());
        this.customerSettingData.setSetting_customer_value(this.customDataFuTableComponent.getDataList());
        this.settingData.setCustomerSettingData(this.customerSettingData);
        FuDocSetting.getInstance().loadState(this.settingData);
    }


    /**
     * 进入该设置页面时会调用该方法
     */
    public void reset() {
    }

}
