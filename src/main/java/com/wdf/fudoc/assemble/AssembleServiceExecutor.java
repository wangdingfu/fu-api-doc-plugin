package com.wdf.fudoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.fudoc.assemble.impl.ControllerAssembleService;
import com.wdf.fudoc.assemble.impl.FeignAssembleService;
import com.wdf.fudoc.assemble.impl.InterfaceAssembleService;
import com.wdf.fudoc.helper.ServiceHelper;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;

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


}
