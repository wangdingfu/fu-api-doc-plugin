package com.wdf.fudoc.apidoc.mock;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.helper.MockDataHelper;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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
        ObjectInfoDesc response = methodInfoDesc.getResponse();
        if (Objects.isNull(response)) {
            return StringUtils.EMPTY;
        }
        return MockDataHelper.mockJsonData(Lists.newArrayList(response));
    }
}
