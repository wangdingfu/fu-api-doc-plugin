package com.wdf.fudoc.request.tab.request;

import cn.hutool.core.util.URLUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.JBColor;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import groovy.util.logging.Slf4j;
import icons.FuDocIcons;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * http请求部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:36
 */
@Slf4j
@Getter
public class RequestTabView implements FuTab, HttpCallback {
    private static final Logger logger = Logger.getInstance(RequestTabView.class);

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
     * Path请求参数tab页
     */
    private final HttpPathParamsTab httpPathParamsTab;
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

    private final JPanel slidePanel;

    public RequestTabView(Project project, SendHttpListener httpListener, JPanel slidePanel, Disposable disposable) {
        this.project = project;
        this.httpListener = httpListener;
        this.slidePanel = slidePanel;
        this.mainPanel = new JPanel(new BorderLayout());
        this.requestTypeComponent = new ComboBox<>(RequestType.getItems());
        this.requestUrlComponent = new JTextField();
        this.sendBtn = new JButton("Send");
        this.httpHeaderTab = new HttpHeaderTab(project, disposable);
        this.httpGetParamsTab = new HttpGetParamsTab(this, disposable);
        this.httpPathParamsTab = new HttpPathParamsTab(this);
        this.httpRequestBodyTab = new HttpRequestBodyTab(disposable);
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
        this.mainPanel.add(fuTabBuilder.addTab(this.httpHeaderTab).addTab(this.httpGetParamsTab).addTab(this.httpPathParamsTab).addTab(this.httpRequestBodyTab).build(), BorderLayout.CENTER);
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
        return FuTabComponent.getInstance("Request", FuDocIcons.HTTP, this.rootPane).builder();
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        if (Objects.nonNull(this.slidePanel)) {
            newSelection.setSideComponent(this.slidePanel);
        }
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
        FuRequestData request = httpRequestData.getRequest();
        this.requestTypeComponent.setSelectedItem(request.getRequestType().getRequestType());
        this.fuHttpRequestData = httpRequestData;
        this.httpHeaderTab.initData(httpRequestData);
        this.httpGetParamsTab.initData(httpRequestData);
        this.httpPathParamsTab.initData(httpRequestData);
        this.httpRequestBodyTab.initData(httpRequestData);
        this.apiUrl = request.getRequestUrl();
        //自动选中tab页
        autoSelectTab(httpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //设置请求类型
        setRequestType(requestTypeComponent.getSelectedItem() + StringUtils.EMPTY);
        httpHeaderTab.doSendBefore(fuHttpRequestData);
        httpGetParamsTab.doSendBefore(fuHttpRequestData);
        httpPathParamsTab.doSendBefore(fuHttpRequestData);
        httpRequestBodyTab.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
    }

    /**
     * 自动定位tab页
     */
    private void autoSelectTab(FuHttpRequestData httpRequestData) {
        if (Objects.isNull(httpRequestData)) {
            return;
        }
        FuRequestData request = httpRequestData.getRequest();
        RequestType requestType = request.getRequestType();
        List<KeyValueTableBO> pathVariables = request.getPathVariables();
        //没有文件上传 且请求类型是GET请求
        if (!request.isFile() && RequestType.GET.equals(requestType)) {
            if (CollectionUtils.isEmpty(request.getParams()) && CollectionUtils.isNotEmpty(pathVariables)) {
                this.fuTabBuilder.select(HttpPathParamsTab.TITLE);
            } else {
                //定位到GET params tab页
                this.fuTabBuilder.select(HttpGetParamsTab.TITLE);
            }
            return;
        }
        FuRequestBodyData body = request.getBody();
        List<KeyValueTableBO> formDataList = body.getFormDataList();
        List<KeyValueTableBO> formUrlEncodedList = body.getFormUrlEncodedList();
        String json = body.getJson();
        if (StringUtils.isBlank(json) && CollectionUtils.isEmpty(formUrlEncodedList) && CollectionUtils.isEmpty(formDataList)) {
            this.fuTabBuilder.select(HttpPathParamsTab.TITLE);
        } else {
            //定位到body tab页
            this.fuTabBuilder.select(HttpRequestBodyTab.BODY);
        }
    }

    /**
     * 初始化组件事件监听器
     */
    private void initComponentEventListener() {

        //对发送按钮添加发起http请求事件
        this.sendBtn.addActionListener(e -> httpListener.doSendHttp());

        //对请求类型按钮添加选项选中事件
        this.requestTypeComponent.addItemListener(e -> changeAction(String.valueOf(e.getItem())));

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
        //todo 修改url时同步更新面板参数
        String requestUrl = this.requestUrlComponent.getText();
        if (Objects.isNull(fuHttpRequestData)) {
            return;
        }
        FuRequestData request = fuHttpRequestData.getRequest();
        if (StringUtils.isBlank(requestUrl)) {
            request.setRequestUrl(StringUtils.EMPTY);
            return;
        }
        request.setRequestUrl(requestUrl);
    }


    private URL parseHttpUrl(String requestUrl) {
        try {
            return URLUtil.toUrlForHttp(requestUrl);
        } catch (Exception e) {
            logger.info("请求地址格式错误", e);
            return null;
        }
    }


    private void resetRequestParam() {
        String requestUrl = this.requestUrlComponent.getText();
        URL url;
        String queryUrl;
        if (StringUtils.isBlank(requestUrl)
                || Objects.isNull(url = parseHttpUrl(requestUrl))
                || StringUtils.isBlank(queryUrl = url.getQuery())) {
            return;
        }
        String path = url.getPath();
        if (StringUtils.isNotBlank(path)) {
            FuTab httpPathTab = this.fuTabBuilder.get(HttpPathParamsTab.TITLE);
            if (Objects.nonNull(httpPathTab)) {
                String[] split = path.split("/");
                Map<String, String> paramMap = new HashMap<>();
                for (String urlItem : split) {
                    if (urlItem.contains("{{") || urlItem.contains("}}")) {
                        continue;
                    }
                    if (StringUtils.startsWith(urlItem, "{") && StringUtils.endsWith(urlItem, "}")) {
                        String name = urlItem.replace("{", "").replace("}", "");
                        paramMap.put(name, "");
                    }
                }
                httpPathTab.resetParams(paramMap);
            }
        }
        FuTab getParamTab = this.fuTabBuilder.get(HttpGetParamsTab.TITLE);
        if (Objects.nonNull(getParamTab)) {
            Map<String, String> paramMap = new HashMap<>();
            for (String params : queryUrl.split("&")) {
                String[] param = params.split("=");
                if (param.length == 2) {
                    paramMap.put(param[0], param[1]);
                }
            }
            //根据请求url回填表格参数
            getParamTab.resetParams(paramMap);
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

    private void changeAction(String requestType) {
        //切换请求参数
        if (RequestType.GET.getRequestType().equals(requestType)) {
            this.fuTabBuilder.select(HttpGetParamsTab.TITLE);
            this.httpRequestBodyTab.clear();
        } else {
            if (Objects.nonNull(this.fuHttpRequestData)) {
                //设置请求类型
                setRequestType(requestType);
                httpGetParamsTab.doSendBefore(fuHttpRequestData);
                httpRequestBodyTab.doSendBefore(fuHttpRequestData);
                this.fuTabBuilder.select(HttpRequestBodyTab.BODY);
            }
        }
    }

}
