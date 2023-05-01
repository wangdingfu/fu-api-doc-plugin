package com.wdf.fudoc.request.tab;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.listener.TabBarListener;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-11-05 07:19:12
 */
public abstract class AbstractBulkEditTabLinkage<T extends KeyValueTableBO> implements TabBarListener {

    /**
     * 获取table组件
     *
     * @param title table所在的tab
     * @return table组件
     */
    protected abstract FuTableComponent<T> getTableComponent(String title);

    /**
     * 获取编辑器组件
     *
     * @param title 编辑器组件所在的tab
     * @return 编辑器组件
     */
    protected abstract FuEditorComponent getEditorComponent(String title);


    /**
     * 切换组件
     * true:  table--->editor
     * false: editor--->table
     *
     * @param tabActionBO tab参数对象
     */
    @Override
    public void onClick(String parentTab, TabActionBO tabActionBO) {
        if (Objects.nonNull(tabActionBO)) {
            if (tabActionBO.isSelect()) {
                //从table组件同步内容到编辑器组件
                getEditorComponent(parentTab).setContent(buildBulkEditContent(getTableComponent(parentTab).getDataList()));
            } else {
                // 从编辑器组件同步到table组件
                bulkEditToTableData(parentTab);
            }
        }
    }

    /**
     * 将编辑器中的数据同步到table中
     */
    protected void bulkEditToTableData(String tab) {
        Map<String, T> keyValueTableBOMap = ObjectUtils.listToMap(getTableComponent(tab).getDataList(), KeyValueTableBO::getKey);
        List<T> tableDataList = editorToTableData(tab);
        if (CollectionUtils.isNotEmpty(tableDataList)) {
            for (T keyValueTableBO : tableDataList) {
                T tableBO = keyValueTableBOMap.get(keyValueTableBO.getKey());
                if (Objects.nonNull(tableBO)) {
                    keyValueTableBO.setDescription(tableBO.getDescription());
                }
            }
        }
        getTableComponent(tab).setDataList(tableDataList);
    }

    protected List<T> editorToTableData() {
        return editorToTableData(StringUtils.EMPTY);
    }

    /**
     * 将编辑器组件的内容转换成表格的数据
     */
    protected List<T> editorToTableData(String tab) {
        return editorToTableDataList(getEditorComponent(tab).getContent());
    }



    protected List<T> editorToTableDataList(String content){
        List<T> dataList = Lists.newArrayList();
        if (StringUtils.isNotBlank(content)) {
            for (String line : content.split("\n")) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                String key = StringUtils.contains(line, ":") ? StringUtils.substringBefore(line, ":") : line;
                String value = StringUtils.substringAfter(line, ":");
                T keyValueTableBO = newInstance();
                formatKey(key, keyValueTableBO);
                keyValueTableBO.setValue(value);
                dataList.add(keyValueTableBO);
            }
        }
        return dataList;
    }

    @SuppressWarnings("all")
    private T newInstance() {
        return ReflectUtil.newInstance((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }


    protected String buildBulkEditContent(List<T> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            return params.stream().map(this::toBulkEdit).collect(Collectors.joining("\n"));
        }
        return StringUtils.EMPTY;
    }


    /**
     * 格式化编辑器中的key
     *
     * @param key             key
     * @param keyValueTableBO key value对象
     */
    private static <T extends KeyValueTableBO> void formatKey(String key, T keyValueTableBO) {
        if (key.startsWith("//")) {
            keyValueTableBO.setSelect(false);
            keyValueTableBO.setKey(StringUtils.replace(key, "//", "").trim());
        } else {
            keyValueTableBO.setSelect(true);
            keyValueTableBO.setKey(key.trim());
        }
    }


    private String toBulkEdit(T keyValueTableBO) {
        String key = keyValueTableBO.getKey();
        String value = keyValueTableBO.getValue();
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        String prefix = keyValueTableBO.getSelect() ? StringUtils.EMPTY : "//";
        return prefix + keyValueTableBO.getKey() + ":" + keyValueTableBO.getValue();
    }
}
