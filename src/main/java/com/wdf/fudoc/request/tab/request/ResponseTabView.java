package com.wdf.fudoc.request.tab.request;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.request.view.ResponseDownloadFileVIew;
import com.wdf.fudoc.request.view.ResponseErrorView;
import com.wdf.fudoc.request.view.ResponseFileView;
import com.wdf.fudoc.util.HttpResponseUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * http响应部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:45
 */
public class ResponseTabView implements FuTab, HttpCallback {

    public static final String RESPONSE = "Response";

    private final Project project;

    @Getter
    private final JPanel rootPanel;

    private final FuEditorComponent fuEditorComponent;

    private final ResponseErrorView responseErrorView;

    private final ResponseFileView responseFileView;

    private Integer tab = 0;

    public ResponseTabView(Project project) {
        this.project = project;
        this.responseErrorView = new ResponseErrorView();
        this.responseFileView = new ResponseFileView();
        this.fuEditorComponent = FuEditorComponent.create(JsonFileType.INSTANCE, "");
        this.rootPanel = new JPanel(new BorderLayout());
        switchPanel(1, this.fuEditorComponent.getMainPanel());
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Response", null, this.rootPanel).builder();
    }


    /**
     * 初始化响应数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuResponseData response = httpRequestData.getResponse();
        ResponseType responseType;
        if (Objects.isNull(response) || Objects.isNull(responseType = response.getResponseType())) {
            return;
        }
        //响应类型
        switch (responseType) {
            case SUCCESS:
                //判断返回结果是文件还是文本
                String fileName = getFileName(response);
                if (StringUtils.isNotBlank(fileName)) {
                    //下载文件
                    fileName = URLUtil.decode(fileName, Charset.defaultCharset());
                    responseFileView.setFileName(fileName);
                    responseFileView.setFuResponseData(response);
                    response.setFileName(fileName);
                    switchPanel(3, responseFileView.getRootPane());
                    initRootPane();
                } else {
                    //请求成功 渲染响应数据到编辑器中
                    fuEditorComponent.setContent(JSONUtil.formatJsonStr(response.getContent()));
                    switchPanel(1, fuEditorComponent.getMainPanel());
                }
                break;
            case ERR_CONNECTION_REFUSED:
                //请求连接被拒绝
                responseErrorView.setErrorDetail(response.getErrorDetail());
                switchPanel(2, responseErrorView.getRootPanel());
                //发送消息
                break;
        }
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //do nothing
    }

    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        if (tab == 3 && Objects.nonNull(responseFileView)) {
            //是文件面板时
            responseFileView.resetDefaultBtn();
        }
    }


    private String getFileName(FuResponseData fuResponseData) {
        String fileName = fuResponseData.getFileName();
        if (StringUtils.isNotBlank(fileName)) {
            return fileName;
        }
        return HttpResponseUtil.getFileNameFromDisposition(fuResponseData.getHttpResponse());
    }

    /**
     * 切换面板
     *
     * @param switchPanel 需要切换的面板
     */
    private void switchPanel(Integer tab, JComponent switchPanel) {
        if (this.tab.equals(tab)) {
            return;
        }
        this.tab = tab;
        this.rootPanel.removeAll();
        this.rootPanel.repaint();
        this.rootPanel.add(switchPanel, BorderLayout.CENTER);
        this.rootPanel.revalidate();
    }


    public void initRootPane(){
        if(Objects.nonNull(responseFileView)){
            responseFileView.initRootPane();
        }
    }

}