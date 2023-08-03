package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import icons.FuDocIcons;

/**
 * @author wangdingfu
 * @date 2023-07-08 15:22:15
 */
public class GlobalCookiesTab implements FuDataTab<FuRequestConfigPO> {


    /**
     * table组件
     */
    private final FuTableComponent<FuCookiePO> fuTableComponent;

    public GlobalCookiesTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.cookie(), FuCookiePO.class);
    }

    @Override
    public void initData(FuRequestConfigPO data) {
        this.fuTableComponent.setDataList(Lists.newArrayList(data.getCookies()));
    }

    @Override
    public void saveData(FuRequestConfigPO data) {
        data.setCookies(Lists.newArrayList(this.fuTableComponent.getDataList()));
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Cookies", FuDocIcons.COOKIE, this.fuTableComponent.createPanel()).builder();
    }
}
