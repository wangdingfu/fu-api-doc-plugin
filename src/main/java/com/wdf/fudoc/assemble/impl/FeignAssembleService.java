package com.wdf.fudoc.assemble.impl;

import com.wdf.fudoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.constant.AnnotationConstants;
import com.wdf.fudoc.constant.enumtype.RequestType;
import com.wdf.fudoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.MethodInfoDesc;


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
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData, AssembleBO assembleBO) {
        return true;
    }

}
