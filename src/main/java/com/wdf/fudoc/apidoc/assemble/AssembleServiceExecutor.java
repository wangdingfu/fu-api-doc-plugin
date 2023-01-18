package com.wdf.fudoc.apidoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.assemble.impl.ControllerAssembleService;
import com.wdf.fudoc.apidoc.assemble.impl.FeignAssembleService;
import com.wdf.fudoc.apidoc.assemble.impl.InterfaceAssembleService;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装接口执行器
 * @date 2022-05-30 23:10:05
 */
public class AssembleServiceExecutor {

    private static final List<FuDocAssembleService> SERVICE_LIST = Lists.newArrayList(
            ServiceHelper.getService(ControllerAssembleService.class),
            ServiceHelper.getService(FeignAssembleService.class),
            ServiceHelper.getService(InterfaceAssembleService.class)
    );


    public static List<FuDocItemData> execute(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        for (FuDocAssembleService fuDocAssembleService : SERVICE_LIST) {
            if (fuDocAssembleService.isAssemble(fuDocContext, classInfoDesc)) {
                return fuDocAssembleService.assemble(fuDocContext, classInfoDesc);
            }
        }
        return Lists.newArrayList();
    }


    public static List<FuDocRootParamData> executeByRequest(FuDocContext fuDocContext, ClassInfoDesc classInfoDesc) {
        for (FuDocAssembleService fuDocAssembleService : SERVICE_LIST) {
            if (fuDocAssembleService.isAssemble(fuDocContext, classInfoDesc)) {
                return fuDocAssembleService.requestAssemble(fuDocContext, classInfoDesc);
            }
        }
        return Lists.newArrayList();
    }


}
