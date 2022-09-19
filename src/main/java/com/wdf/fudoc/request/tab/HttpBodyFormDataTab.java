package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.FuTableView;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.factory.TableCellEditorFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;

import javax.swing.table.TableCellEditor;

/**
 * @author wangdingfu
 * @date 2022-09-18 22:54:51
 */
public class HttpBodyFormDataTab implements FuTab, InitRequestData {

    /**
     * 请求参数table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;

    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    public HttpBodyFormDataTab() {
        //请求参数表格组件初始化
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.formDataColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        this.fuTableComponent.addListener(new BodyFormDataTableListener());
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("form-data", null, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel()).builder();
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {

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
