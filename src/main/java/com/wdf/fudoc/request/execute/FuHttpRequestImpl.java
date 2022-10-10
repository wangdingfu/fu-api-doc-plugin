package com.wdf.fudoc.request.execute;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * 发起http请求实现类
 *
 * @author wangdingfu
 * @date 2022-09-19 20:26:08
 */
public class FuHttpRequestImpl implements FuHttpRequest {

    /**
     * 请求状态 用于控制按钮显示正在请求中
     */
    private boolean requestStatus;

    /**
     * 请求所需数据
     */
    private final FuHttpRequestData fuHttpRequestData;

    /**
     * 当前项目
     */
    private final Project project;

    public FuHttpRequestImpl(Project project, FuHttpRequestData fuHttpRequestData) {
        this.project = project;
        this.fuHttpRequestData = fuHttpRequestData;
    }

    /**
     * 获取当前请求状态
     *
     * @return true 正在请求中 false 请求完成
     */
    @Override
    public boolean getStatus() {
        return this.requestStatus;
    }

    /**
     * 发起请求
     *
     * @param httpCallback 请求完成回调业务逻辑
     */
    @Override
    public void doSend(HttpCallback httpCallback) {
        //设置当前正在请求中
        this.requestStatus = true;
        //执行发起请求前置逻辑
        httpCallback.doSendBefore(this.fuHttpRequestData);
        //发起请求
        HttpExecutor.execute(this.fuHttpRequestData);
        //执行请求后置逻辑
        httpCallback.doSendAfter(this.fuHttpRequestData);
        //结束请求 释放相关资源
        finished();
    }

    /**
     * 请求完毕 需要释放相关资源等操作
     */
    @Override
    public void finished() {
        //保存当前请求
        FuRequestManager.saveRequest(project, fuHttpRequestData);
        this.requestStatus = false;
    }


    /**
     * 中断请求
     */
    @Override
    public void doStop() {
        //中断请求的线程
        finished();
    }
}
