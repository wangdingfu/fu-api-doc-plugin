package com.wdf.fudoc.request.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;

/**
 * http请求body tab
 *
 * @author wangdingfu
 * @date 2022-09-17 21:32:03
 */
public class HttpRequestBodyTab implements FuTab, InitRequestData {

    public static final String BODY = "Body";

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


    /**
     * 初始化body部分数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {

    }
}
