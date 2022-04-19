package com.wdf.apidoc.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @descption: 生成API接口文档的数据
 * @author wangdingfu
 * @date 2022-04-05 19:58:19
 */
@Getter
@Setter
public class ApiDocData extends AnnotationDataMap{

    /**
     * 当前文档标题
     */
    private String title;

    /**
     * 解析后的方法属性数据集合
     */
    private List<ApiDocMethodData> apiDocMethodDataList;
}
