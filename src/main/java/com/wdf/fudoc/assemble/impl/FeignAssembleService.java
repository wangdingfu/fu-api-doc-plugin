package com.wdf.fudoc.assemble.impl;

import com.wdf.fudoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuApiDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装Feign接口文档
 * @date 2022-05-09 23:33:38
 */
public class FeignAssembleService extends AbstractAssembleService {


    @Override
    public boolean isAssemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        return classInfoDesc.exists(AnnotationConstants.FEIGN_CLIENT);
    }

    @Override
    public List<FuApiDocItemData> assemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        return null;
    }
}
