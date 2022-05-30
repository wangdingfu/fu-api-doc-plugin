package com.wdf.apidoc.assemble.impl;

import com.wdf.apidoc.assemble.AbstractAssembleService;
import com.wdf.apidoc.pojo.bo.AssembleBO;
import com.wdf.apidoc.pojo.data.FuApiDocData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;

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
