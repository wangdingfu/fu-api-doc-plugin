package com.wdf.fudoc.test.view;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.project.Project;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import com.wdf.fudoc.components.tree.FuModuleTreeStructure;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-01-07 19:54:31
 */
public class TestPanel {

    @Getter
    private JPanel rootPanel;

    private final StructureTreeModel<AbstractTreeStructure> treeModel;
    private final SimpleTree catalogTree;


    public TestPanel(Project project) {
        rootPanel = new JPanel(new BorderLayout());
        treeModel = new StructureTreeModel<>(new FuModuleTreeStructure(project),null,project);
        catalogTree = new SimpleTree(new AsyncTreeModel(treeModel, project));
        initCatalogTree();
        rootPanel.add(catalogTree);
    }

    public TreePath[] getSelected(){
        return catalogTree.getSelectionPaths();
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
