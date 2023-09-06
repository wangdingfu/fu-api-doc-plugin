package com.wdf.fudoc.futool.beancopy.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-08-07 11:13:59
 */
@Getter
@Setter
public class FuPsiMethod {

    private boolean isGetter;
    private boolean isSetter;
    /**
     * 字段名
     */
    private String fieldName;


    public boolean isFieldMethod() {
        return isGetter || isSetter;
    }

}
