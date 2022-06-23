package com.wdf.fudoc.assemble.impl;

import com.wdf.fudoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.pojo.data.FuApiDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装dubbo接口文档
 * @date 2022-05-09 23:34:55
 */
public class DubboAssembleService extends AbstractAssembleService {


    @Override
    public boolean isAssemble(ClassInfoDesc classInfoDesc) {
        return false;
    }

    @Override
    public List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc) {
        return null;
    }
}
