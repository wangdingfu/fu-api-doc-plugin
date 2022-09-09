package com.wdf.fudoc.common.components;

import com.intellij.util.ui.ColumnInfo;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:20:25
 */
public abstract class EditableColumnInfo<Item, Aspect> extends ColumnInfo<Item, Aspect> {

    public EditableColumnInfo(String name) {
        super(name);
    }

    public EditableColumnInfo() {
        super("");
    }


    @Override
    public boolean isCellEditable(Item item) {
        return true;
    }
}
