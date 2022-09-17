package com.wdf.fudoc.request.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;

/**
 * http请求参数tab页
 *
 * @author wangdingfu
 * @date 2022-09-17 21:31:31
 */
public class HttpParamsTab implements FuTab {

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Params", null, FuComponentsUtils.createEmptyTable())
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, FuComponentsUtils.createEmptyEditor())
                .builder();
    }
}
