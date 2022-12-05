package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.FuTableView;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.factory.TableCellEditorFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;

import javax.swing.table.TableCellEditor;

/**
 * @author wangdingfu
 * @date 2022-11-25 21:42:31
 */
public class ChooseFileTableUtils {


    public static FuTableComponent<KeyValueTableBO> createTableComponents() {
        //请求参数表格组件初始化
        FuTableComponent<KeyValueTableBO> fuTableComponent = FuTableComponent.create(FuTableColumnFactory.formDataColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        fuTableComponent.addListener(new BodyFormDataTableListener());
        return fuTableComponent;
    }


    /**
     * table组件监听器
     */
    static class BodyFormDataTableListener implements FuTableListener<KeyValueTableBO> {
        @Override
        public TableCellEditor getCellEditor(FuTableView<KeyValueTableBO> fuTableView, int row, int column) {
            if (column == 3) {
                Object valueAt = fuTableView.getValueAt(row, column - 1);
                if (RequestParamType.FILE.getCode().equals(valueAt)) {
                    return TableCellEditorFactory.createLocalPathCellEditor();
                }
            }
            return null;
        }
    }


}
