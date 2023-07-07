package com.wdf.fudoc.request.execute;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuConsole;
import com.wdf.fudoc.request.js.JsExecutor;
import com.wdf.fudoc.request.js.context.FuContext;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-19 19:57:13
 */
@Slf4j
public class HttpApiExecutor {


    /**
     * 发起请求
     *
     * @param project           当前项目
     * @param fuHttpRequestData 请求数据
     */
    public static void doSendRequest(Project project, FuHttpRequestData fuHttpRequestData, FuConsole fuConsole) {
        //执行前置脚本
        long start = System.currentTimeMillis();
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(project);
        FuRequestConfigPO fuRequestConfigPO = fuRequestConfigStorage.readData();
        List<GlobalPreScriptPO> preScriptPOList;
        Module module = fuHttpRequestData.getModule();
        if (Objects.nonNull(module) && Objects.nonNull(fuRequestConfigPO)
                && CollectionUtils.isNotEmpty(preScriptPOList = fuRequestConfigPO.getPreScriptList(module.getName()))) {
            for (GlobalPreScriptPO globalPreScriptPO : preScriptPOList) {
                fuConsole.info("\n开始执行前置脚本【{}】", globalPreScriptPO.getTitle());
                long scriptStart = System.currentTimeMillis();
                JsExecutor.execute(new FuContext(project, fuRequestConfigPO, globalPreScriptPO), fuConsole);
                fuConsole.info("执行前置脚本【{}】完成 共计耗时:{}ms\n", globalPreScriptPO.getTitle(), System.currentTimeMillis() - scriptStart);
            }
        }
        log.info("执行脚本共计耗时:{}ms", System.currentTimeMillis() - start);
        //发起请求
        long start1 = System.currentTimeMillis();
        HttpExecutor.execute(project, fuHttpRequestData, fuRequestConfigPO, fuConsole);
        log.info("发起[{}]接口请求共计耗时:{}ms", fuHttpRequestData.getApiName(), System.currentTimeMillis() - start1);
    }
}
