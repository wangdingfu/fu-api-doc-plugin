package com.wdf.fudoc.components.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-07-08 22:17:25
 */
public class CustomTableSettingDialog extends DialogWrapper {

    private final String title;

    private final Project project;

    private final FuTableComponent<KeyValueTableBO> configTable;

    public CustomTableSettingDialog(@Nullable Project project, String title) {
        super(project, true);
        this.project = project;
        this.title = title;
        this.configTable = FuTableComponent.create(FuTableColumnFactory.customConfig(), KeyValueTableBO.class);
        init();
        setTitle("自定义表头设置");
        initData();
    }


    private void initData() {
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(this.project);
        Map<String, List<KeyValueTableBO>> customTableConfigMap = fuRequestConfigStorage.readData().getCustomTableConfigMap();
        List<KeyValueTableBO> keyValueTableBOList = customTableConfigMap.get(this.title);
        if (CollectionUtils.isEmpty(keyValueTableBOList)) {
            keyValueTableBOList = Lists.newArrayList();
            customTableConfigMap.put(this.title, keyValueTableBOList);
        }
        this.configTable.setDataList(keyValueTableBOList);
    }


    public List<KeyValueTableBO> getColumnList() {
        return this.configTable.getDataList();
    }

    @Override
    protected void doOKAction() {
        //持久化数据
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(this.project);
        Map<String, List<KeyValueTableBO>> customTableConfigMap = fuRequestConfigStorage.readData().getCustomTableConfigMap();
        customTableConfigMap.put(this.title, getColumnList());
        fuRequestConfigStorage.saveData();
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
