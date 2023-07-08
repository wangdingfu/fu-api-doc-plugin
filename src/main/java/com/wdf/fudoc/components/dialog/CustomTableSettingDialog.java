package com.wdf.fudoc.components.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.common.po.FuDocConfigPO;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.storage.FuDocConfigStorage;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-07-08 22:17:25
 */
public class CustomTableSettingDialog extends DialogWrapper {

    private final String title;

    private final FuTableComponent<KeyValueTableBO> configTable;

    public CustomTableSettingDialog(@Nullable Project project, String title) {
        super(project, true);
        this.title = title;
        this.configTable = FuTableComponent.create(FuTableColumnFactory.customConfig(), KeyValueTableBO.class);
        init();
        setTitle("自定义表头设置");
        initData();
    }


    private void initData() {
        FuDocConfigPO fuDocConfigPO = FuDocConfigStorage.INSTANCE.readData();
        List<KeyValueTableBO> keyValueTableBOList = fuDocConfigPO.getCustomTableConfigMap().get(this.title);
        if (CollectionUtils.isEmpty(keyValueTableBOList)) {
            return;
        }
        this.configTable.setDataList(Lists.newArrayList(keyValueTableBOList));
    }


    public List<KeyValueTableBO> getColumnList() {
        return this.configTable.getDataList();
    }

    @Override
    protected void doOKAction() {
        //持久化数据
        FuDocConfigStorage instance = FuDocConfigStorage.INSTANCE;
        Map<String, List<KeyValueTableBO>> customTableConfigMap = instance.readData().getCustomTableConfigMap();
        List<KeyValueTableBO> keyValueTableBOList = customTableConfigMap.get(this.title);
        if (CollectionUtils.isEmpty(keyValueTableBOList)) {
            keyValueTableBOList = Lists.newArrayList();
            customTableConfigMap.put(this.title, keyValueTableBOList);
        }
        instance.saveData();
        super.doOKAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return configTable.createPanel();
    }
}
