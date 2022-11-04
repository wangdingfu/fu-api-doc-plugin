package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.listener.TabBarListener;
import com.wdf.fudoc.test.view.bo.BarPanelBO;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-11-05 07:19:12
 */
public abstract class AbstractBulkEditTabLinkage implements TabBarListener {
    protected abstract FuTableComponent<KeyValueTableBO> getTableComponent();

    protected abstract FuEditorComponent getEditorComponent();


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
            if (barPanelBO.isSelect()) {
                //从table组件同步内容到编辑器组件
                getEditorComponent().setContent(buildBulkEditContent(getTableComponent().getDataList()));
            } else {
                // 从编辑器组件同步到table组件
                bulkEditToTableData();
            }
        }
    }



    /**
     * 将编辑器中的数据同步到table中
     */
    private void bulkEditToTableData() {
        Map<String, KeyValueTableBO> keyValueTableBOMap = ObjectUtils.listToMap(getTableComponent().getDataList(), KeyValueTableBO::getKey);
        List<KeyValueTableBO> tableDataList = editorToTableData();
        if (CollectionUtils.isNotEmpty(tableDataList)) {
            for (KeyValueTableBO keyValueTableBO : tableDataList) {
                KeyValueTableBO tableBO = keyValueTableBOMap.get(keyValueTableBO.getKey());
                if (Objects.nonNull(tableBO)) {
                    keyValueTableBO.setDescription(tableBO.getDescription());
                }
            }
        }
        getTableComponent().setDataList(tableDataList);
    }




    /**
     * 将编辑器组件的内容转换成表格的数据
     */
    protected List<KeyValueTableBO> editorToTableData() {
        String content = getEditorComponent().getContent();
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


    protected String buildBulkEditContent(List<KeyValueTableBO> params) {
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
    private static void formatKey(String key, KeyValueTableBO keyValueTableBO) {
        if (key.startsWith("//")) {
            keyValueTableBO.setSelect(false);
            keyValueTableBO.setKey(StringUtils.replace(key, "//", "").trim());
        } else {
            keyValueTableBO.setSelect(true);
            keyValueTableBO.setKey(key);
        }
    }



    private String toBulkEdit(KeyValueTableBO keyValueTableBO) {
        String prefix = keyValueTableBO.getSelect() ? StringUtils.EMPTY : "//";
        return prefix + keyValueTableBO.getKey() + ":" + keyValueTableBO.getValue();
    }
}
