package com.wdf.fudoc.request.execute;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.CommonResult;
import com.wdf.fudoc.request.global.FuRequest;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;

import java.util.Objects;

/**
 * 发起http请求实现类
 *
 * @author wangdingfu
 * @date 2022-09-19 20:26:08
 */
public class FuHttpRequestImpl implements FuHttpRequest {

    private boolean requestStatus;

    private final FuHttpRequestData fuHttpRequestData;

    private final Project project;

    public FuHttpRequestImpl(Project project, FuHttpRequestData fuHttpRequestData) {
        this.project = project;
        this.fuHttpRequestData = fuHttpRequestData;
    }

    @Override
    public boolean getStatus() {
        return this.requestStatus;
    }

    @Override
    public void doSend() {
        this.requestStatus = true;
        //发起请求
        ThreadUtil.execAsync(() -> {
            //填充请求结果
            FuResponseData response = this.fuHttpRequestData.getResponse();
            if (Objects.isNull(response)) {
                response = new FuResponseData();
                this.fuHttpRequestData.setResponse(response);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //设置响应结果
            response.setContent(JSONUtil.toJsonStr(CommonResult.ok()));
            finished();
        });
    }

    @Override
    public void finished() {
        this.requestStatus = false;
        FuRequest.remove(this.project);

    }

    @Override
    public void doStop() {
        //中断请求的线程
        finished();
    }
}
