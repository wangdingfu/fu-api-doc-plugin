package com.wdf.fudoc.apidoc.mock;

import com.wdf.fudoc.apidoc.pojo.bo.MockResultBo;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;

/**
 * mock接口数据接口
 *
 * @author wangdingfu
 * @date 2022-11-16 22:45:51
 */
public interface MockDataService {

    /**
     * mock指定接口的数据
     *
     * @param methodInfoDesc 指定接口的方法描述信息对象
     * @param fuDocItemData  指定接口数据对象
     * @return mock的数据
     */
    MockResultBo mockData(MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData);
}
