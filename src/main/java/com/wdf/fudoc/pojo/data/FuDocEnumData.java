package com.wdf.fudoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-07-29 18:11:13
 */
@Getter
@Setter
public class FuDocEnumData {

    /**
     * 枚举标题
     */
    private String title;

    /**
     * 枚举名称
     */
    private String enumName;

    /**
     * 枚举明细
     */
    private List<FuDocEnumItemData> itemList;
}
