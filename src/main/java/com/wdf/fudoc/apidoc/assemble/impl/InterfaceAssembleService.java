package com.wdf.fudoc.apidoc.assemble.impl;

import com.wdf.fudoc.apidoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.apidoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.CommonItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;

/**
 * @author wangdingfu
 * @date 2022-07-20 16:50:28
 */
public class InterfaceAssembleService extends AbstractAssembleService {

    @Override
    public boolean isAssemble(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        return classInfoDesc.getPsiClass().isInterface();
    }


    @Override
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, CommonItemData commonItemData, AssembleBO assembleBO) {
        return true;
    }

}
