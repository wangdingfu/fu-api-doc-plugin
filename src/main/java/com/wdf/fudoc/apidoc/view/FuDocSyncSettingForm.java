package com.wdf.fudoc.apidoc.view;

import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.view.tab.ShowDocSettingsTab;
import com.wdf.fudoc.apidoc.view.tab.SyncApiTopView;
import com.wdf.fudoc.apidoc.view.tab.YApiSettingTab;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
    private final JPanel rootPanel;

    /**
     * YApi配置页面
     */
    private final YApiSettingTab yApiSettingsTab;
    /**
     * ShowDoc配置页面
     */
    private final ShowDocSettingsTab showDocSettingsTab;

    public FuDocSyncSettingForm() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.yApiSettingsTab = new YApiSettingTab();
        this.showDocSettingsTab = new ShowDocSettingsTab();
        FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance();
        fuTabBuilder.addTab(this.yApiSettingsTab).addTab(this.showDocSettingsTab);
        //需要判断是否展示提示信息
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        if (Objects.isNull(enableConfigData) || StringUtils.isBlank(enableConfigData.getBaseUrl()) || !enableConfigData.isExistsConfig()) {
            this.rootPanel.add(new SyncApiTopView().getRootPanel(), BorderLayout.NORTH);
        }
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
