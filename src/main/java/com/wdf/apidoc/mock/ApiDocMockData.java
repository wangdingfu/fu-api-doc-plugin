package com.wdf.apidoc.mock;

import com.wdf.apidoc.constant.enumtype.ContentType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 针对指定类型mock数据
 * @date 2022-05-17 23:01:08
 */
public interface ApiDocMockData {


    /**
     * mock数据
     *
     * @param contentType        请求数据类型(http内容类型)
     * @param objectInfoDescList 对象描述信息
     * @return mock后的数据示例
     */
    String mock(ContentType contentType, List<ObjectInfoDesc> objectInfoDescList);
}
