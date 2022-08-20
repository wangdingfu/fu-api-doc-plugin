package com.wdf.fudoc.view.action;

import com.wdf.fudoc.data.CustomerSettingData;

/**
 * @author wangdingfu
 * @date 2022-08-20 17:06:34
 */
public abstract class AbstractFuTableAction<T> implements FuTableAction<T> {

    /**
     * 自定义数据对象
     */
    private CustomerSettingData customerSettingData;

}
