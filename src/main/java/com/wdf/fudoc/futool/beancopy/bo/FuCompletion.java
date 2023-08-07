package com.wdf.fudoc.futool.beancopy.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-08-06 11:45:25
 */
@Getter
@Setter
@NoArgsConstructor
public class FuCompletion {

    private CopyBeanBO copyBean;
    private CopyBeanBO toBean;
    private int index;

    public FuCompletion(CopyBeanBO copyBean, CopyBeanBO toBean, int index) {
        this.copyBean = copyBean;
        this.toBean = toBean;
        this.index = index;
    }
}
