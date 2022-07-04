package com.wdf.fudoc.pojo.desc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 类信息描述
 * @date 2022-05-08 22:21:40
 */
@Getter
@Setter
public class ClassInfoDesc extends BaseInfoDesc {

    /**
     * 类ID
     */
    private String classId;

    /**
     * 方法集合
     */
    private List<MethodInfoDesc> methodList;
}
