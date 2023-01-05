package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.json.JsonFileType;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.bo.KeyValueTableBO;

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
        return component.createPanel();
    }

    public static JPanel createEmptyEditor() {
        return FuEditorComponent.create(JsonFileType.INSTANCE, "").getMainPanel();
    }
}
