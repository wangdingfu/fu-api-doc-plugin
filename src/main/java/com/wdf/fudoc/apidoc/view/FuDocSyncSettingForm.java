package com.wdf.fudoc.apidoc.view;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.view.tab.ShowDocSettingsTab;
import com.wdf.fudoc.apidoc.view.tab.YApiSettingTab;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import com.wdf.fudoc.util.FuMessageUtils;
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
            this.rootPanel.add(FuMessageUtils.createMessage(buildMsg()), BorderLayout.NORTH);
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


    private FuMsgBO buildMsg() {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId("fudoc.sync.api.tip.noConfig");
        fuMsgBO.setWeight(1d);
        fuMsgBO.setItemList(Lists.newArrayList(buildTipInfo(), buildDocLink()));
        return fuMsgBO;
    }


    private FuMsgItemBO buildTipInfo() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("您还未添加接口文档系统相关配置 暂时还不能同步接口哦 ");
        fuMsgItemBO.setMsgId("fudoc.sync.api.tip.noConfig.title");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        //红色
        fuMsgItemBO.setRegularColor(FuColor.RED.getRegularColor());
        fuMsgItemBO.setDarkColor(FuColor.RED.getDarkColor());
        return fuMsgItemBO;
    }

    private FuMsgItemBO buildDocLink() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("点我快速了解如何添加这些配置吧!");
        fuMsgItemBO.setMsgId("fudoc.sync.api.tip.noConfig.link");
        fuMsgItemBO.setValue(UrlConstants.FU_DOCUMENT_SYNC_URL);
        fuMsgItemBO.setMsgType(MessageType.LINK.getCode());
        fuMsgItemBO.setRegularColor(FuColor.GITHUB.getRegularColor());
        fuMsgItemBO.setDarkColor(FuColor.GITHUB.getDarkColor());
        return fuMsgItemBO;
    }

}
