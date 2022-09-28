package com.wdf.fudoc.apidoc.assemble.impl;

import com.wdf.fudoc.apidoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.CommonItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;


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
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, CommonItemData commonItemData, AssembleBO assembleBO) {
        return true;
    }

}
