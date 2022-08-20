package com.wdf.fudoc.view;

import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingDynamicValueData;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.view.components.FuTableComponent;
import lombok.Getter;

import javax.swing.*;

/**
 * Fu Doc 基础设置页面
 *
 * @author wangdingfu
 * @date 2022-08-14 21:31:11
 */
public class FuDocGeneralForm {

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

    /**
     * 自定义持久化数据
     */
    private final CustomerSettingData customerSettingData;


    public FuDocGeneralForm(CustomerSettingData customerSettingData) {
        this.customerSettingData = customerSettingData;
    }


    /**
     * 系统会自动调用该方法来完成自定义panel的创建
     */
    private void createUIComponents() {
        //创建过滤属性面板
        this.filterPanel = FuTableComponent.create(FuTableColumnFactory.filterColumns(), customerSettingData.getSettings_filter_field(), FilterFieldBO.class);
        //创建自定义数据面板
        this.customDataPanel = FuTableComponent.create(FuTableColumnFactory.dynamicColumns(), customerSettingData.getSetting_customer_value(), SettingDynamicValueData.class);
    }




    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
    }


    /**
     * 进入该设置页面时会调用该方法
     */
    public void reset() {
    }

}
