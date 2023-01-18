package com.wdf.fudoc.apidoc.mock.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * mock参数对象
 *
 * @author wangdingfu
 * @date 2023-01-13 13:49:48
 */
@Getter
@Setter
public class MockParamDataBO {
    /**
     * 字段名
     */
    private String fileName;

    /**
     * 字段类型
     */
    private String fileType;

    /**
     * mock的值
     */
    private String mockValue;
}
