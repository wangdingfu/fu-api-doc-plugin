package com.wdf.fudoc.view.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-09-01 11:43:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueTableBO {

    /**
     * 默认选中
     */
    private Boolean select = true;

    /**
     * key
     */
    private String key;

    /**
     * value
     */
    private String value;

    /**
     * 描述信息
     */
    private String description;
}
