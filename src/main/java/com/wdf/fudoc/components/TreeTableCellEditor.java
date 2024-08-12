package com.wdf.fudoc.components;

import com.google.common.collect.Lists;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.components.tree.old.FuModuleTreeComponent;
import com.wdf.fudoc.components.tree.old.FuTableTreeComponent;
import cn.fudoc.common.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-07 16:17:22
 */
public class TreeTableCellEditor extends AbstractTableCellEditor {


    private final FuTableTreeComponent fuTableTreeComponent;
    private final FuModuleTreeComponent fuModuleTreeComponent;

    public TreeTableCellEditor() {
        //构建module树组件
        this.fuModuleTreeComponent = new FuModuleTreeComponent(ProjectUtils.getCurrProject());
        this.fuTableTreeComponent = new FuTableTreeComponent(this.fuModuleTreeComponent);
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (Objects.nonNull(value) && value instanceof TreePathBO treePathBO) {
            List<String> selectPathList = treePathBO.getSelectPathList();
            if(CollectionUtils.isNotEmpty(selectPathList)){
                TreePath[] treePaths = new TreePath[selectPathList.size()];
                for (int i = 0; i < selectPathList.size(); i++) {
                    Integer moduleIndex = this.fuModuleTreeComponent.getModuleIndex(selectPathList.get(i));
                    if(Objects.isNull(moduleIndex)){
                        break;
                    }
                    TreePath treePath = this.fuTableTreeComponent.getSimpleTree().getPathForRow(moduleIndex);
                    if(Objects.isNull(treePath)){
                        break;
                    }
                    treePaths[i] = treePath;
                }
                fuTableTreeComponent.setSelectedItem(treePaths);
            }
        }
        return fuTableTreeComponent;
    }


    @Override
    public Object getCellEditorValue() {
        TreePath[] selected = fuTableTreeComponent.getSelectedItem();
        List<String> selectPathList = Lists.newArrayList();
        if (Objects.nonNull(selected)) {
            for (TreePath treePath : selected) {
                Object lastPathComponent = treePath.getLastPathComponent();
                if (Objects.nonNull(lastPathComponent)) {
                    selectPathList.add(lastPathComponent.toString());
                }
            }
        }
        return new TreePathBO(selectPathList);
    }


}
