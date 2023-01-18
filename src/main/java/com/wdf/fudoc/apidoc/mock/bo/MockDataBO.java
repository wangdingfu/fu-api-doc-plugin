package com.wdf.fudoc.apidoc.mock.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 接口mock的数据
 *
 * @author wangdingfu
 * @date 2023-01-13 13:47:23
 */
@Getter
@Setter
public class MockDataBO {

    /**
     * GET请求参数mock数据集合
     */
    private List<MockParamDataBO> headerList;

    /**
     * GET请求pathVariable参数mock数据集合
     */
    private List<MockParamDataBO> pathVariableList;
    /**
     * GET请求参数mock数据集合
     */
    private List<MockParamDataBO> getParamList;
    /**
     * POST请求 form-data格式参数mock数据集合
     */
    private List<MockParamDataBO> formDataList;


}
