package com.wdf.fudoc.apidoc.view;

import com.wdf.fudoc.apidoc.view.tab.ShowDocSettingsTab;
import com.wdf.fudoc.apidoc.view.tab.YApiSettingTab;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Fu Doc 同步相关配置
 *
 * @author wangdingfu
 * @date 2023-01-06 23:05:34
 */
public class FuDocSyncSettingForm {

    /**
     * 跟节点
     */
    @Getter
    private JPanel rootPanel;


    /**
     * tab页构建器
     */
    private final FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance();


    /**
     * YApi配置页面
     */
    private YApiSettingTab yApiSettingsTab;
    /**
     * ShowDoc配置页面
     */
    private ShowDocSettingsTab showDocSettingsTab;

    public FuDocSyncSettingForm() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.yApiSettingsTab = new YApiSettingTab();
        this.showDocSettingsTab = new ShowDocSettingsTab();
        fuTabBuilder.addTab(this.yApiSettingsTab).addTab(this.showDocSettingsTab);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
    }

    public void apply() {
        yApiSettingsTab.apply();

        showDocSettingsTab.apply();
    }


    public void reset() {
        yApiSettingsTab.reset();

        showDocSettingsTab.reset();
    }

}
