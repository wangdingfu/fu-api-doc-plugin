package com.wdf.fudoc.apidoc.mock.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-13 13:54:34
 */
@Getter
@Setter
public class MockJsonDataBO extends MockParamDataBO {

    /**
     * 子对象
     */
    private MockJsonDataBO item;

    /**
     * 数组
     */
    private List<MockJsonDataBO> array;

    /**
     * 当前对象的字段mock数据集合
     */
    private List<MockParamDataBO> properties;

}
