package com.wdf.fudoc.pojo.bo;

import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装参数
 * @date 2022-05-09 23:30:21
 */
@Getter
@Setter
public class AssembleBO {

    /**
     * 标题
     */
    private String title;

    /**
     * java类信息集合
     */
    private List<ClassInfoDesc> classInfoDescList;
}
