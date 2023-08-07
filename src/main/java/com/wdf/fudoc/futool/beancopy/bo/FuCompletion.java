package com.wdf.fudoc.futool.beancopy.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-08-06 11:45:25
 */
@Getter
@Setter
public class FuCompletion {

    private final CopyBeanBO copyBean;
    private final CopyBeanBO toBean;
    private final int index;

    public FuCompletion(CopyBeanBO copyBean, CopyBeanBO toBean, int index) {
        this.copyBean = copyBean;
        this.toBean = toBean;
        this.index = index;
    }
}
