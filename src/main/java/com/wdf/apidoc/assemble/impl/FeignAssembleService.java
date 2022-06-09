package com.wdf.apidoc.assemble.impl;

import com.wdf.apidoc.assemble.AbstractAssembleService;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.pojo.bo.AssembleBO;
import com.wdf.apidoc.pojo.data.FuApiDocData;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.util.FuDocUtils;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装Feign接口文档
 * @date 2022-05-09 23:33:38
 */
public class FeignAssembleService extends AbstractAssembleService {

    @Override
    public boolean isAssemble(ClassInfoDesc classInfoDesc) {
        return classInfoDesc.exists(AnnotationConstants.FEIGN_CLIENT);
    }

    @Override
    public List<FuApiDocItemData> assemble(ClassInfoDesc classInfoDesc) {
        return null;
    }
}
