package com.wdf.fudoc.pojo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 该类的数据用于渲染接口文档，即当前类已经存储了接口文档中所需要的所有数据
 *
 * @author wangdingfu
 * @descption: FuApiDoc接口文档对象
 * @date 2022-05-08 23:02:36
 */
@Getter
@Setter
public class FuApiDocData {

    /**
     * 文档标题
     */
    private String title;


    /**
     * 接口列表集合
     */
    private List<FuDocItemData> itemList;

}
