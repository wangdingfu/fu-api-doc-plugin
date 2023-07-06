package com.wdf.fudoc.components.tree.old;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import lombok.Getter;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;

/**
 * 树形组件
 *
 * @author wangdingfu
 * @date 2023-01-07 20:33:13
 */
public class FuTreeComponent<T extends FuTreeNode> extends JComponent {


    @Getter
    private final SimpleTree catalogTree;

    @Getter
    private final T root;


    public FuTreeComponent(Project project, T root) {
        super();
        this.root = root;
        FuTreeStructure<FuTreeNode> fuTreeStructure = new FuTreeStructure<>(root);
        catalogTree = new SimpleTree(new AsyncTreeModel(new StructureTreeModel<>(fuTreeStructure, null, project), project));
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
