package com.wdf.fudoc.assemble.impl;

import com.wdf.fudoc.assemble.AbstractAssembleService;
import com.wdf.fudoc.constant.enumtype.RequestType;
import com.wdf.fudoc.pojo.bo.AssembleBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.MethodInfoDesc;

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
    protected boolean doAssembleInfoMethod(FuDocContext fuDocContext, MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData, AssembleBO assembleBO) {
        return true;
    }

}
