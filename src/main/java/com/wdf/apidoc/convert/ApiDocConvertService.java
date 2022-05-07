package com.wdf.apidoc.convert;

import com.wdf.apidoc.pojo.data.ApiDocData;

/**
 * @author wangdingfu
 * @descption: 根据公共解析对象数据转换成生成接口文档的数据对象
 * @date 2022-05-07 23:24:36
 */
public interface ApiDocConvertService {

    ApiDocData convert();
}
