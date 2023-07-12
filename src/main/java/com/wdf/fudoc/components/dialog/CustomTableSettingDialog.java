package com.wdf.fudoc.components.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-07-08 22:17:25
 */
public class CustomTableSettingDialog extends DialogWrapper implements FuTableListener<KeyValueTableBO> {

    private final String title;

    private final Project project;

    private final FuTableComponent<KeyValueTableBO> configTable;

    public CustomTableSettingDialog(@Nullable Project project, String title, List<KeyValueTableBO> dataList) {
        super(project, true);
        this.project = project;
        this.title = title;
        this.configTable = FuTableComponent.create(null, FuTableColumnFactory.customConfig(), KeyValueTableBO.class, this);
        init();
        setTitle("自定义表头设置");
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.removeIf(f -> StringUtils.isBlank(f.getKey()) || StringUtils.isBlank(f.getValue()));
            this.configTable.setDataList(dataList);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        KeyValueTableBO data = this.configTable.getData(rowIndex);
        return Objects.nonNull(data) && data.isEditable;
    }

    @Override
    public boolean canExchangeRows(int oldIndex, int newIndex) {
        return isCellEditable(oldIndex, 0) && isCellEditable(newIndex, 0);
    }

    public List<KeyValueTableBO> getColumnList() {
        List<KeyValueTableBO> dataList = this.configTable.getDataList();
        return dataList.stream().filter(KeyValueTableBO::isEditable).collect(Collectors.toList());
    }

    @Override
    protected void doOKAction() {
        //持久化数据
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(this.project);
        Map<String, List<KeyValueTableBO>> customTableConfigMap = fuRequestConfigStorage.readData().getCustomTableConfigMap();
        customTableConfigMap.put(this.title, getColumnList());
        super.doOKAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = this.configTable.createPanel();
        panel.setMinimumSize(new Dimension(300, 200));
        panel.setPreferredSize(new Dimension(400, 300));
        return panel;
    }


}
