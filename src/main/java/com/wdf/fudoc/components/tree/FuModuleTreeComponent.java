package com.wdf.fudoc.components.tree;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import lombok.Getter;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author wangdingfu
 * @date 2023-01-07 20:33:13
 */
public class FuModuleTreeComponent extends JComponent {


    @Getter
    private final SimpleTree catalogTree;

    @Getter
    private final ModuleNode root;


    public FuModuleTreeComponent(Project project) {
        super();
        FuModuleTreeStructure fuModuleTreeStructure = new FuModuleTreeStructure(project);
        this.root = fuModuleTreeStructure.getRoot();
        catalogTree = new SimpleTree(new AsyncTreeModel(new StructureTreeModel<>(fuModuleTreeStructure, null, project), project));
        initCatalogTree();
    }

    /**
     * 初始化目录树
     */
    private void initCatalogTree() {
        catalogTree.setRootVisible(true);
        catalogTree.setShowsRootHandles(true);
        catalogTree.getEmptyText().clear();
        catalogTree.setBorder(BorderFactory.createEmptyBorder());
        catalogTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }
}
