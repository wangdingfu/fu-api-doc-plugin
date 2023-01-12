package com.wdf.fudoc.components;

import com.intellij.util.ui.AbstractTableCellEditor;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.components.tree.FuModuleTreeComponent;
import com.wdf.fudoc.components.tree.FuTableTreeComponent;
import com.wdf.fudoc.util.ProjectUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-07 16:17:22
 */
public class TreeTableCellEditor extends AbstractTableCellEditor {

    private final FuTableTreeComponent fuTableTreeComponent;

    public TreeTableCellEditor() {
        //构建module树组件
        this.fuTableTreeComponent = new FuTableTreeComponent(new FuModuleTreeComponent(ProjectUtils.getCurrProject()).getCatalogTree());
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (Objects.nonNull(value) && value instanceof TreePathBO) {
            TreePathBO treePathBO = (TreePathBO) value;
            fuTableTreeComponent.setSelectedItem(treePathBO.getSelectPath());
        }
        return fuTableTreeComponent;
    }


    @Override
    public Object getCellEditorValue() {
        TreePath[] selected = fuTableTreeComponent.getSelectedItem();
        return new TreePathBO(selected);
    }


}
