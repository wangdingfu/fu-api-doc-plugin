package com.wdf.fudoc.request.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;

/**
 * http请求头tab页
 *
 * @author wangdingfu
 * @date 2022-09-17 21:30:58
 */
public class HttpHeaderTab implements FuTab {


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", null, FuComponentsUtils.createEmptyTable())
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, FuComponentsUtils.createEmptyEditor())
                .builder();
    }
}
