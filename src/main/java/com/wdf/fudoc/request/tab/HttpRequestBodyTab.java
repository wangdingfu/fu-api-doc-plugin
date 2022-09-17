package com.wdf.fudoc.request.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;

/**
 * http请求body tab
 *
 * @author wangdingfu
 * @date 2022-09-17 21:32:03
 */
public class HttpRequestBodyTab implements FuTab {
    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Body", null, FuComponentsUtils.createEmptyEditor())
                .addToggleBar("none", FuDocIcons.FU_REQUEST_IGNORE, FuComponentsUtils.createEmptyTable())
                .addToggleBar("form-data", FuDocIcons.FU_REQUEST_FORM, FuComponentsUtils.createEmptyTable1())
                .addToggleBar("x-www-form-urlencoded", FuDocIcons.FU_REQUEST_URLENCODED, FuComponentsUtils.createEmptyTable())
                .addToggleBar("raw", FuDocIcons.FU_REQUEST_RAW, FuComponentsUtils.createEmptyEditor())
                .addToggleBar("json", FuDocIcons.FU_REQUEST_JSON, FuComponentsUtils.createEmptyEditor())
                .addToggleBar("binary", FuDocIcons.FU_REQUEST_FILE_BINARY, FuComponentsUtils.createEmptyTable())
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, FuComponentsUtils.createEmptyEditor())
                .setDefaultTab("json").builder();
    }
}
