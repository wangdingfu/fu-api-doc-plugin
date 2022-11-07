package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.listener.TabBarListener;
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

    /**
     * 获取table组件
     *
     * @param title table所在的tab
     * @return table组件
     */
    protected abstract FuTableComponent<KeyValueTableBO> getTableComponent(String title);

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
    private void bulkEditToTableData(String tab) {
        Map<String, KeyValueTableBO> keyValueTableBOMap = ObjectUtils.listToMap(getTableComponent(tab).getDataList(), KeyValueTableBO::getKey);
        List<KeyValueTableBO> tableDataList = editorToTableData(tab);
        if (CollectionUtils.isNotEmpty(tableDataList)) {
            for (KeyValueTableBO keyValueTableBO : tableDataList) {
                KeyValueTableBO tableBO = keyValueTableBOMap.get(keyValueTableBO.getKey());
                if (Objects.nonNull(tableBO)) {
                    keyValueTableBO.setDescription(tableBO.getDescription());
                }
            }
        }
        getTableComponent(tab).setDataList(tableDataList);
    }

    protected List<KeyValueTableBO> editorToTableData() {
        return editorToTableData(StringUtils.EMPTY);
    }

    /**
     * 将编辑器组件的内容转换成表格的数据
     */
    protected List<KeyValueTableBO> editorToTableData(String tab) {
        String content = getEditorComponent(tab).getContent();
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
