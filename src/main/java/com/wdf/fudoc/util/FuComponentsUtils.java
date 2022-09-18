package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.json.JsonFileType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.factory.TableCellEditorFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-17 21:46:13
 */
public class FuComponentsUtils {

    public static JPanel createEmptyTable() {
        return FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
    }


    public static JPanel createEmptyTable1() {
        FuTableComponent<KeyValueTableBO> component = FuTableComponent.create(FuTableColumnFactory.formDataColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        component.addListener((fuTableView, row, column) -> {
            if (column == 3) {
                Object valueAt = fuTableView.getValueAt(row, column - 1);
                if (RequestParamType.FILE.getCode().equals(valueAt)) {
                    return TableCellEditorFactory.createLocalPathCellEditor();
                }
            }
            return null;
        });
        return component.createPanel();
    }

    public static JPanel createEmptyEditor() {
        return FuEditorComponent.create(JsonFileType.INSTANCE, "").getMainPanel();
    }
}
