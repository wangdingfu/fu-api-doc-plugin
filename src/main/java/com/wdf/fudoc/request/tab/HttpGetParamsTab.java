package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.listener.FuEditorListener;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.components.listener.TabBarListener;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.spring.SpringConfigFileManager;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.BarPanelBO;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.PathUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * http请求参数tab页【GET请求参数】
 *
 * @author wangdingfu
 * @date 2022-09-17 21:31:31
 */
public class HttpGetParamsTab implements FuTab, InitRequestData, TabBarListener {
    /**
     * 请求参数table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;

    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    /**
     * 请求tab页
     */
    private final RequestTabView requestTabView;

    /**
     * http请求数据对象
     */
    private FuHttpRequestData httpRequestData;

    /**
     * 请求地址 不携带请求参数
     */
    private String requestBaseUrl;


    /**
     * 初始化GET请求的table组件和批量编辑组件
     *
     * @param requestTabView 父容器对象
     */
    public HttpGetParamsTab(RequestTabView requestTabView) {
        this.requestTabView = requestTabView;
        //请求参数表格组件初始化
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        this.fuTableComponent.addListener(new HttpGetParamsTableListener(this));
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
        this.fuEditorComponent.addListener(new HttpGetEditorListener(this));
    }

    /**
     * 将当前容器作为一个tab返回出去
     */
    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Params", null, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel(), this).builder();
    }


    /**
     * 初始化请求头数据
     *
     * @param httpRequestData http请求数据对象
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.httpRequestData = httpRequestData;
        FuRequestData request = httpRequestData.getRequest();
        List<KeyValueTableBO> params = request.getParams();
        if (CollectionUtils.isNotEmpty(params)) {
            this.fuTableComponent.setDataList(params);
            this.fuEditorComponent.setContent(buildBulkEditContent(params));
        }
        //生成请求地址
        genRequestBaseUrl(httpRequestData);
        //重置接口请求地址
        resetRequestUrlFromTable();
    }


    /**
     * 切换组件
     * true:  table--->editor
     * false: editor--->table
     *
     * @param barPanelBO 批量编辑按钮对象
     */
    @Override
    public void click(BarPanelBO barPanelBO) {
        if (Objects.nonNull(barPanelBO)) {
            reset(barPanelBO.isSelect());
        }

    }


    /**
     * table组件监听器 当table组件中的数据发生变化时 需要更新url
     */
    static class HttpGetParamsTableListener implements FuTableListener<KeyValueTableBO> {

        private final HttpGetParamsTab httpGetParamsTab;

        public HttpGetParamsTableListener(HttpGetParamsTab httpGetParamsTab) {
            this.httpGetParamsTab = httpGetParamsTab;
        }

        @Override
        public void propertyChange(KeyValueTableBO data, int row, int column, Object value) {
            //table属性发生了变更 需要重新生成请求地址
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void deleteRow(int row) {
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void exchangeRows(int oldIndex, int newIndex) {
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }
    }


    /**
     * 编辑器监听器 当编辑器中的文本内容发生变化时 需要更新请求url
     */
    static class HttpGetEditorListener implements FuEditorListener {
        private final HttpGetParamsTab httpGetParamsTab;

        public HttpGetEditorListener(HttpGetParamsTab httpGetParamsTab) {
            this.httpGetParamsTab = httpGetParamsTab;
        }

        @Override
        public void contentChange(String content) {
            //重置请求地址
            httpGetParamsTab.resetRequestUrlFromEditor();
        }
    }

    /**
     * 重置且同步table组件和批量编辑组件内容
     *
     * @param isTable true 从table组件同步到批量编辑组件 false 从批量编辑组件同步到table组件
     */
    public void reset(boolean isTable) {
        if (isTable) {
            //从table组件同步内容到编辑器组件
            this.fuEditorComponent.setContent(buildBulkEditContent(this.fuTableComponent.getDataList()));
        } else {
            // 从编辑器组件同步到table组件
            bulkEditToTableData();
        }
        //重置请求地址
        resetRequestUrlFromTable();
    }


    /**
     * 格式化编辑器中的key
     *
     * @param key             key
     * @param keyValueTableBO key value对象
     */
    private static void formatKey(String key, KeyValueTableBO keyValueTableBO) {
        if (key.startsWith("//")) {
            keyValueTableBO.setSelect(false);
            keyValueTableBO.setKey(StringUtils.replace(key, "//", "").trim());
        } else {
            keyValueTableBO.setSelect(true);
            keyValueTableBO.setKey(key);
        }
    }


    /**
     * 生成不携带请求参数的请求地址
     *
     * @param httpRequestData http请求数据对象
     */
    private void genRequestBaseUrl(FuHttpRequestData httpRequestData) {
        String moduleId = httpRequestData.getModuleId();
        Integer serverPort = SpringConfigFileManager.getServerPort(moduleId);
        FuRequestData request = httpRequestData.getRequest();
        String apiUrl = request.getApiUrl();
        //接口地址中参数替换
        List<KeyValueTableBO> pathVariables = request.getPathVariables();
        if (CollectionUtils.isNotEmpty(pathVariables)) {
            for (KeyValueTableBO pathVariable : pathVariables) {
                String key = pathVariable.getKey();
                StringUtils.replace(apiUrl, "{" + key + "}", pathVariable.getValue());
            }
        }
        this.requestBaseUrl = PathUtils.joinUrl(FuDocConstants.DEFAULT_HOST + ":" + serverPort, apiUrl);
    }


    /**
     * 依据table组件的数据来重置请求地址
     */
    public void resetRequestUrlFromTable() {
        resetRequestUrl(joinParamsFromTable());
    }

    /**
     * 依据编辑器组件的数据来重置请求地址
     */
    public void resetRequestUrlFromEditor() {
        resetRequestUrl(joinParamsFromEditor());
    }


    /**
     * 根据表格组件的请求参数拼接成请求url追加的参数
     */
    public String joinParamsFromTable() {
        return joinParams(this.fuTableComponent.getDataList());
    }

    /**
     * 根据编辑器组件的请求参数拼接成请求url追加的参数
     */
    private String joinParamsFromEditor() {
        return joinParams(editorToTableData());
    }


    /**
     * 拼接请求参数为GET请求参数格式
     *
     * @param dataList 请求参数集合
     * @return url后追加的参数
     */
    private String joinParams(List<KeyValueTableBO> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            String paramUrl = dataList.stream().filter(KeyValueTableBO::getSelect).map(this::buildKeyValue).collect(Collectors.joining("&"));
            if (StringUtils.isNotBlank(paramUrl)) {
                return "?" + paramUrl;
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 重置请求地址
     */
    public void resetRequestUrl(String paramUrl) {
        if (Objects.nonNull(this.httpRequestData)) {
            //完整请求地址=域名+apiUrl+请求参数
            String requestUrl = PathUtils.joinUrl(this.requestBaseUrl, paramUrl);
            FuRequestData request = this.httpRequestData.getRequest();
            request.setRequestUrl(requestUrl);
            this.requestTabView.setRequestUrl(requestUrl);
        }
    }


    /**
     * 将编辑器中的数据同步到table中
     */
    private void bulkEditToTableData() {
        Map<String, KeyValueTableBO> keyValueTableBOMap = ObjectUtils.listToMap(this.fuTableComponent.getDataList(), KeyValueTableBO::getKey);
        List<KeyValueTableBO> tableDataList = editorToTableData();
        if (CollectionUtils.isNotEmpty(tableDataList)) {
            for (KeyValueTableBO keyValueTableBO : tableDataList) {
                KeyValueTableBO tableBO = keyValueTableBOMap.get(keyValueTableBO.getKey());
                if (Objects.nonNull(tableBO)) {
                    keyValueTableBO.setDescription(tableBO.getDescription());
                }
            }
        }
        this.fuTableComponent.setDataList(tableDataList);
    }


    /**
     * 将编辑器组件的内容转换成表格的数据
     */
    private List<KeyValueTableBO> editorToTableData() {
        String content = this.fuEditorComponent.getContent();
        List<KeyValueTableBO> dataList = Lists.newArrayList();
        if (StringUtils.isNotBlank(content)) {
            for (String line : content.split("\n")) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                String key = StringUtils.contains(line, ":") ? StringUtils.substringBefore(line, ":") : line;
                String value = StringUtils.substringAfter(line, ":");
                KeyValueTableBO keyValueTableBO = new KeyValueTableBO();
                formatKey(key, keyValueTableBO);
                keyValueTableBO.setValue(value);
                dataList.add(keyValueTableBO);
            }
        }
        return dataList;
    }


    private String buildBulkEditContent(List<KeyValueTableBO> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            return params.stream().map(this::toBulkEdit).collect(Collectors.joining("\n"));
        }
        return StringUtils.EMPTY;
    }

    private String toBulkEdit(KeyValueTableBO keyValueTableBO) {
        String prefix = keyValueTableBO.getSelect() ? StringUtils.EMPTY : "//";
        return prefix + keyValueTableBO.getKey() + ":" + keyValueTableBO.getValue();
    }

    private String buildKeyValue(KeyValueTableBO keyValueTableBO) {
        return keyValueTableBO.getKey() + "=" + keyValueTableBO.getValue();
    }

}
