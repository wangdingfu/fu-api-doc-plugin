package com.wdf.fudoc.request.execute;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
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
    public static void doSendRequest(Project project, FuHttpRequestData fuHttpRequestData) {
        //执行前置脚本
        long start = System.currentTimeMillis();
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(project);
        FuRequestConfigPO fuRequestConfigPO = fuRequestConfigStorage.readData();
        List<GlobalPreScriptPO> preScriptPOList;
        Module module = fuHttpRequestData.getModule();
        if (Objects.nonNull(module) && Objects.nonNull(fuRequestConfigPO)
                && CollectionUtils.isNotEmpty(preScriptPOList = fuRequestConfigPO.getPreScriptList(module.getName()))) {
            for (GlobalPreScriptPO globalPreScriptPO : preScriptPOList) {
                JsExecutor.execute(new FuContext(project, fuRequestConfigPO, globalPreScriptPO));
            }
        }
        log.info("执行脚本共计耗时:{}ms", System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        //发起请求
        HttpExecutor.execute(project, fuHttpRequestData, fuRequestConfigPO);
        log.info("发起请求共计耗时:{}ms", System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        //持久化数据
        fuRequestConfigStorage.saveData(fuRequestConfigPO);
        log.info("持久化数据共计耗时:{}ms", System.currentTimeMillis() - start);

    }
}
