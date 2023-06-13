package com.wdf.fudoc.request.tab.request;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.JBColor;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.execute.HttpApiExecutor;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * http请求部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:36
 */
@Getter
public class RequestTabView implements FuTab, HttpCallback {

    private final Project project;

    private final JRootPane rootPane;

    private final JPanel mainPanel;

    /**
     * 请求类型
     */
    private final JComboBox<String> requestTypeComponent;

    /**
     * 请求地址
     */
    private final JTextField requestUrlComponent;

    /**
     * 发送按钮
     */
    private final JButton sendBtn;

    /**
     * 请求头tab页
     */
    private final HttpHeaderTab httpHeaderTab;
    /**
     * GET请求参数tab页
     */
    private final HttpGetParamsTab httpGetParamsTab;
    /**
     * POST请求参数tab页
     */
    private final HttpRequestBodyTab httpRequestBodyTab;
    /**
     * tab页构建器
     */
    private final FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance();
    /**
     * api接口url
     */
    private String apiUrl;

    /**
     * 发起http请求的数据对象
     */
    private FuHttpRequestData fuHttpRequestData;

    /**
     * 发送请求后回调逻辑
     */
    private final SendHttpListener httpListener;


    private final FuRequestStatusInfoView fuRequestStatusInfoView;

    public RequestTabView(Project project, SendHttpListener httpListener, FuRequestStatusInfoView fuRequestStatusInfoView) {
        this.project = project;
        this.fuRequestStatusInfoView = fuRequestStatusInfoView;
        this.httpListener = httpListener;
        this.mainPanel = new JPanel(new BorderLayout());
        this.requestTypeComponent = new ComboBox<>(RequestType.getItems());
        this.requestUrlComponent = new JTextField();
        this.sendBtn = new JButton("Send");
        this.httpHeaderTab = new HttpHeaderTab();
        this.httpGetParamsTab = new HttpGetParamsTab(this);
        this.httpRequestBodyTab = new HttpRequestBodyTab();
        this.rootPane = new JRootPane();
        initRootPane();
        initUI();
        //初始化相关组件的事件监听器
        initComponentEventListener();
    }


    private void initUI() {
        //send区域
        this.mainPanel.add(initSendPanel(), BorderLayout.NORTH);
        //请求参数区域
        this.mainPanel.add(fuTabBuilder.addTab(this.httpHeaderTab).addTab(this.httpGetParamsTab).addTab(this.httpRequestBodyTab).build(), BorderLayout.CENTER);
    }

    private JPanel initSendPanel() {
        JPanel sendPane = new JPanel(new BorderLayout());
        //请求类型
        this.requestTypeComponent.setBackground(new JBColor(new Color(74, 136, 199), new Color(74, 136, 199)));
        sendPane.add(this.requestTypeComponent, BorderLayout.WEST);
        //请求url
        sendPane.add(this.requestUrlComponent, BorderLayout.CENTER);
        //send按钮
        sendPane.add(this.sendBtn, BorderLayout.EAST);
        return sendPane;
    }


    @Override
    public TabInfo getTabInfo() {
        JPanel jPanel = Objects.isNull(this.fuRequestStatusInfoView) ? null : this.fuRequestStatusInfoView.getRootPanel();
        return FuTabComponent.getInstance("Request", null, this.rootPane).builder(jPanel);
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        rootPane.setDefaultButton(sendBtn);
    }


    public void setRequestUrl(String requestUrl) {
        this.requestUrlComponent.setText(requestUrl);
    }


    public void initRootPane() {
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.mainPanel);
        rootPane.setDefaultButton(this.getSendBtn());
    }

    /**
     * 初始化请求数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.fuHttpRequestData = httpRequestData;
        FuRequestData request = httpRequestData.getRequest();
        this.requestTypeComponent.setSelectedItem(request.getRequestType().getRequestType());
        this.apiUrl = request.getRequestUrl();
        this.httpHeaderTab.initData(httpRequestData);
        this.httpGetParamsTab.initData(httpRequestData);
        this.httpRequestBodyTab.initData(httpRequestData);
        //自动选中tab页
        autoSelectTab(httpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //设置请求类型
        setRequestType(requestTypeComponent.getSelectedItem() + StringUtils.EMPTY);
        httpHeaderTab.doSendBefore(fuHttpRequestData);
        httpGetParamsTab.doSendBefore(fuHttpRequestData);
        httpRequestBodyTab.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        this.fuRequestStatusInfoView.initData(fuHttpRequestData);
    }

    /**
     * 自动定位tab页
     */
    private void autoSelectTab(FuHttpRequestData httpRequestData) {
        FuRequestData request = httpRequestData.getRequest();
        RequestType requestType = request.getRequestType();
        //没有文件上传 且请求类型是GET请求
        if (!request.isFile() && RequestType.GET.equals(requestType)) {
            //定位到GET params tab页
            this.fuTabBuilder.select(HttpGetParamsTab.PARAMS);
            return;
        }
        //定位到body tab页
        this.fuTabBuilder.select(HttpRequestBodyTab.BODY);
    }

    /**
     * 初始化组件事件监听器
     */
    private void initComponentEventListener() {

        //对发送按钮添加发起http请求事件
        this.sendBtn.addActionListener(e -> httpListener.doSendHttp());

        //对请求类型按钮添加选项选中事件
        this.requestTypeComponent.addItemListener(e -> setRequestType(String.valueOf(e.getItem())));

        //对请求地址添加属性内容变更事件
        this.requestUrlComponent.addFocusListener(new FocusListener() {
            String beforeFocusValue;

            @Override
            public void focusGained(FocusEvent e) {
                beforeFocusValue = requestUrlComponent.getText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentValue = requestUrlComponent.getText();
                if (!currentValue.equals(beforeFocusValue)) {
                    resetRequestParam();
                }
            }
        });


        this.requestUrlComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resetRequestUrl();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resetRequestUrl();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resetRequestUrl();
            }
        });
    }


    private void resetRequestUrl() {
        String requestUrl = this.requestUrlComponent.getText();
        FuRequestData request = fuHttpRequestData.getRequest();
        if (StringUtils.isBlank(requestUrl)) {
            request.setParamUrl(null);
            return;
        }
        request.setBaseUrl(StringUtils.substringBefore(requestUrl, "?"));
        request.setParamUrl(StringUtils.substringAfter(requestUrl, "?"));
    }


    private void resetRequestParam() {
        String requestUrl = this.requestUrlComponent.getText();
        String paramUrl = StringUtils.substringAfter(requestUrl, "?");
        FuTab selected = this.fuTabBuilder.getSelected();
        if (Objects.nonNull(selected)) {
            Map<String, String> paramMap = new HashMap<>();
            for (String params : paramUrl.split("&")) {
                String[] param = params.split("=");
                if (param.length == 2) {
                    paramMap.put(param[0], param[1]);
                }
            }
            //根据请求url回填表格参数
            selected.resetParams(paramMap);
        }
    }


    /**
     * 设置请求类型
     *
     * @param requestType 请求类型
     */
    private void setRequestType(String requestType) {
        if (Objects.nonNull(this.fuHttpRequestData)) {
            FuRequestData request = fuHttpRequestData.getRequest();
            request.setRequestType(RequestType.getRequestType(requestType));
        }
    }

}
