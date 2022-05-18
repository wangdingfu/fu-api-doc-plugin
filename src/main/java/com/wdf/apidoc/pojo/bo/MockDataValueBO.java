package com.wdf.apidoc.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @Descption mock 数据值对象
 * @Date 2022-05-18 21:46:58
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MockDataValueBO {

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数值
     */
    private Object value;




}
