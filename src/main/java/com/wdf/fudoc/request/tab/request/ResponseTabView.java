package com.wdf.fudoc.request.tab.request;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wdf.fudoc.compat.JsonFileTypeCompat;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.request.view.ResponseContentView;
import com.wdf.fudoc.request.view.ResponseErrorView;
import com.wdf.fudoc.request.view.ResponseFileView;
import com.wdf.fudoc.util.ResourceUtils;
import icons.FuDocIcons;
import lombok.Getter;
import com.wdf.fudoc.util.FuStringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * http响应部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:45
 */
@Slf4j
public class ResponseTabView implements FuTab, HttpCallback {

    public static final String RESPONSE = "Response";

    private final Project project;

    @Getter
    private final JPanel rootPanel;

    private final FuEditorComponent fuEditorComponent;

    private final ResponseContentView responseContentView;

    private final ResponseErrorView responseErrorView;

    private final ResponseFileView responseFileView;

    private final JPanel slidePanel;

    private Integer tab = 0;

    // 当前内容类型
    private String currentContentType;

    public ResponseTabView(Project project, JPanel slidePanel, Disposable disposable) {
        this.project = project;
        this.slidePanel = slidePanel;
        this.responseErrorView = new ResponseErrorView(disposable);
        this.responseFileView = new ResponseFileView();
        this.responseContentView = new ResponseContentView(project, disposable);
        this.fuEditorComponent = FuEditorComponent.create(JsonFileTypeCompat.getJsonFileType(), "", disposable);
        this.rootPanel = new JPanel(new BorderLayout());
        switchPanel(1, this.fuEditorComponent.getMainPanel());
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Response", FuDocIcons.RESPONSE, this.rootPanel).builder();
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
            case SUCCESS -> {
                String fileName = response.getFileName();
                if (FuStringUtils.isNotBlank(fileName)) {
                    //响应结果是文件
                    response.setFileName(fileName);
                    HttpResponse httpResponse = response.getHttpResponse();
                    //issue:#22 解决第二次进入下载界面时由于没有缓存文件字节流导致空指针异常问题
                    if (Objects.nonNull(httpResponse)) {
                        //将文件暂存到临时目录
                        String suffix = FileUtil.getSuffix(fileName);
                        File tmpFile = ResourceUtils.createFuRequestFileDir(project.getName(), suffix);
                        FileUtil.writeBytes(httpResponse.bodyBytes(), tmpFile);
                        response.setFilePath(tmpFile.getPath());
                    }
                    //响应面板切换到文件下载面板
                    responseFileView.setFileName(fileName);
                    responseFileView.setFuResponseData(response);
                    switchPanel(3, responseFileView.getRootPane());
                    initRootPane();
                } else {
                    //请求成功 渲染响应数据到编辑器中
                    String content = response.getContent();
                    if (FuStringUtils.isBlank(content)) {
                        fuEditorComponent.setContent("");
                        switchPanel(1, fuEditorComponent.getMainPanel());
                    } else {
                        // 获取内容类型
                        String contentType = response.getContentType();
                        currentContentType = contentType;

                        try {
                            // 检查内容长度
                            if (content.length() > MAX_CONTENT_LENGTH) {
                                log.warn("响应内容过长，长度: {}, 将截断显示", content.length());
                                // 使用新的响应内容视图（支持大文件）
                                responseContentView.setContent(content, contentType);
                                switchPanel(4, responseContentView.getComponent());
                            } else {
                                // 判断是否为JSON格式
                                if (isJsonContent(content, contentType)) {
                                    // JSON格式，使用格式化
                                    String formattedContent = formatJsonContent(content);
                                    fuEditorComponent.setContent(formattedContent);
                                    switchPanel(1, fuEditorComponent.getMainPanel());
                                } else {
                                    // 非JSON格式，使用新的响应内容视图
                                    responseContentView.setContent(content, contentType);
                                    switchPanel(4, responseContentView.getComponent());
                                }
                            }
                        } catch (Exception e) {
                            log.error("渲染响应内容失败", e);
                            // 出错时使用原始内容
                            fuEditorComponent.setContent(content);
                            switchPanel(1, fuEditorComponent.getMainPanel());
                        }
                    }
                }
            }
            case ERR_CONNECTION_REFUSED -> {
                //请求连接被拒绝
                responseErrorView.setErrorDetail(response.getErrorDetail());
                switchPanel(2, responseErrorView.getRootPanel());
            }
        }
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        // 请求发送前预留钩子
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        // 请求完成后,initData 会被调用来显示响应结果
    }

    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        if (tab == 3 && Objects.nonNull(responseFileView)) {
            //是文件面板时
            responseFileView.resetDefaultBtn();
        }
        if (Objects.nonNull(this.slidePanel)) {
            newSelection.setSideComponent(this.slidePanel);
        }
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


    public void initRootPane() {
        if (Objects.nonNull(responseFileView)) {
            responseFileView.initRootPane();
        }
    }

    // 最大显示内容长度（超过则截断显示）
    private static final int MAX_CONTENT_LENGTH = 2 * 1024 * 1024; // 2MB
    // JSON 格式快速检测的内容阈值（超过此长度不进行深度检测）
    private static final int JSON_QUICK_CHECK_THRESHOLD = 100 * 1024; // 100KB

    /**
     * 判断是否为JSON内容
     * 优化：对于大文件只进行简单格式检测，避免完整解析导致的性能问题
     *
     * @param content 内容
     * @param contentType 内容类型
     * @return 是否为JSON
     */
    private boolean isJsonContent(String content, String contentType) {
        // 首先根据Content-Type判断（最可靠的方式）
        if (FuStringUtils.isNotBlank(contentType) && contentType.toLowerCase().contains("application/json")) {
            return true;
        }

        // 根据内容判断
        if (FuStringUtils.isNotBlank(content)) {
            String trimmedContent = content.trim();

            // 快速检查：是否以 JSON 格式的开头和结尾字符
            boolean looksLikeJson = (trimmedContent.startsWith("{") && trimmedContent.endsWith("}")) ||
                                    (trimmedContent.startsWith("[") && trimmedContent.endsWith("]"));

            if (!looksLikeJson) {
                return false;
            }

            // 对于小文件，进行简单的格式验证
            // 对于大文件，直接根据首尾字符判断，避免性能问题
            if (content.length() <= JSON_QUICK_CHECK_THRESHOLD) {
                // 简单验证：检查是否包含基本的 JSON 结构特征
                // 避免将 XML 或 HTML 误判为 JSON
                return !trimmedContent.contains("<?xml") && !trimmedContent.contains("<!DOCTYPE");
            }

            // 大文件直接返回 true（已通过首尾字符检查）
            return true;
        }

        return false;
    }

    /**
     * 格式化 JSON 内容
     * 避免长字符串换行时导致格式问题
     *
     * @param content 原始内容
     * @return 格式化后的内容
     */
    private String formatJsonContent(String content) {
        if (FuStringUtils.isBlank(content)) {
            return content;
        }
        try {
            // IDEA 2025.1+ 修复: 使用 Jackson 进行 JSON 格式化
            // Jackson 会正确处理字符串中的转义字符,不会将 \r\n 展开为真实换行
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 先解析 JSON,再格式化输出
            Object json = mapper.readValue(content, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            // 如果 Jackson 格式化失败,尝试使用 Hutool
            try {
                if (JSONUtil.isTypeJSON(content)) {
                    return JSONUtil.formatJsonStr(content);
                }
            } catch (Exception ex) {
                // 忽略
            }
            // 都失败则返回原始内容
            return content;
        }
    }

}
