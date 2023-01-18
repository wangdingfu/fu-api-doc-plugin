package com.wdf.fudoc.apidoc.mock;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.helper.MockDataHelper;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;

/**
 * mock接口数据实现
 *
 * @author wangdingfu
 * @date 2022-11-16 22:48:38
 */
public class AutoMockDataServiceImpl extends AbstractMockDataService {


    @Override
    protected String mockRequest(RequestType requestType, MethodInfoDesc methodInfoDesc) {
        return MockDataHelper.mockRequestData(requestType, methodInfoDesc.getRequestList());
    }

    @Override
    protected String mockResponse(MethodInfoDesc methodInfoDesc) {
        return MockDataHelper.mockJsonData(Lists.newArrayList(methodInfoDesc.getResponse()));
    }
}
