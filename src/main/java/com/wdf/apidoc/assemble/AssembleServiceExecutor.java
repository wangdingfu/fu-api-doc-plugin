package com.wdf.apidoc.assemble;

import com.google.common.collect.Lists;
import com.wdf.apidoc.assemble.impl.ControllerAssembleService;
import com.wdf.apidoc.assemble.impl.DubboAssembleService;
import com.wdf.apidoc.assemble.impl.FeignAssembleService;
import com.wdf.apidoc.helper.ServiceHelper;
import com.wdf.apidoc.pojo.data.FuApiDocItemData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 组装接口执行器
 * @date 2022-05-30 23:10:05
 */
public class AssembleServiceExecutor {

    private static final List<ApiDocAssembleService> SERVICE_LIST = Lists.newArrayList(
            ServiceHelper.getService(ControllerAssembleService.class),
            ServiceHelper.getService(FeignAssembleService.class),
            ServiceHelper.getService(DubboAssembleService.class)
    );



    public static List<FuApiDocItemData> execute(ClassInfoDesc classInfoDesc) {
        for (ApiDocAssembleService apiDocAssembleService : SERVICE_LIST) {
            if (apiDocAssembleService.isAssemble(classInfoDesc)) {
                return apiDocAssembleService.assemble(classInfoDesc);
            }
        }
        return Lists.newArrayList();
    }


}
