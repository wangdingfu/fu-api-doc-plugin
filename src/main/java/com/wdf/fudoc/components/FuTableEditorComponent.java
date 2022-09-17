package com.wdf.fudoc.components;

import com.intellij.util.Function;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.TableModelEditor;
import com.wdf.fudoc.request.view.bo.KeyValueTableBO;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-05 18:31:04
 */
public class FuTableEditorComponent {



    private static final ColumnInfo[] COLUMNS = {
            new BooleanColumnInfo<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect),
            new StringColumnInfo<>("KEY", KeyValueTableBO::getKey, KeyValueTableBO::setKey),
            new StringColumnInfo<>("VALUE", KeyValueTableBO::getValue, KeyValueTableBO::setValue),
            new StringColumnInfo<>("DESCRIPTION", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription)
    };

    public static JComponent createUIComponents() {
        TableModelEditor.DialogItemEditor<KeyValueTableBO> itemEditor = new TableModelEditor.DialogItemEditor<>() {
            @Override
            public void edit(@NotNull KeyValueTableBO item, @NotNull Function<? super KeyValueTableBO, ? extends KeyValueTableBO> mutator, boolean isAdd) {
                System.out.println("edit");
            }

            @Override
            public void applyEdited(@NotNull KeyValueTableBO oldItem, @NotNull KeyValueTableBO newItem) {
                System.out.println("applyEdited");
            }

            @Override
            public @NotNull Class<? extends KeyValueTableBO> getItemClass() {
                return KeyValueTableBO.class;
            }

            @Override
            public KeyValueTableBO clone(@NotNull KeyValueTableBO item, boolean forInPlaceEditing) {
                return new KeyValueTableBO();
            }
        };
        TableModelEditor<KeyValueTableBO> browsersEditor = new TableModelEditor<>(COLUMNS, itemEditor, "");
        browsersEditor.setShowGrid(true);
        return browsersEditor.createComponent();
    }
}
