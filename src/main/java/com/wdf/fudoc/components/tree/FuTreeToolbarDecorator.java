package com.wdf.fudoc.components.tree;

import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.CommonActionsPanel;
import com.intellij.ui.RowsDnDSupport;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.EditableModel;
import com.intellij.util.ui.EditableTreeModel;
import com.intellij.util.ui.ElementProducer;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.Arrays;

/**
 * @author wangdingfu
 * @date 2023-07-02 00:43:33
 */
public class FuTreeToolbarDecorator extends ToolbarDecorator {

    private final JComponent myComponent;
    private final JTree myTree;
    @Nullable
    private final ElementProducer<?> myProducer;

    private final EditableTreeModel treeModel;

    public FuTreeToolbarDecorator(JTree myTree, EditableTreeModel treeModel) {
        this(myTree, treeModel, null);
    }

    FuTreeToolbarDecorator(JTree tree, EditableTreeModel treeModel, @Nullable final ElementProducer<?> producer) {
        this(tree, tree, treeModel, producer);
    }

    FuTreeToolbarDecorator(@NotNull JComponent component, @NotNull JTree tree, EditableTreeModel treeModel, @Nullable final ElementProducer<?> producer) {
        myComponent = component;
        myTree = tree;
        myProducer = producer;
        this.treeModel = treeModel;
        myAddActionEnabled = myRemoveActionEnabled = myUpActionEnabled = myDownActionEnabled = true;
        createDefaultTreeActions();
        myTree.getSelectionModel().addTreeSelectionListener(e -> updateButtons());
        myTree.addPropertyChangeListener("enabled", evt -> updateButtons());
    }

    private void createDefaultTreeActions() {
        myAddAction = button -> {
            final TreePath path = myTree.getSelectionPath();
            final DefaultMutableTreeNode selected =
                    path == null ? (DefaultMutableTreeNode) myTree.getModel().getRoot() : (DefaultMutableTreeNode) path.getLastPathComponent();
            myTree.stopEditing();
            final TreePath createdPath = treeModel.addNode(new TreePath(selected.getPath()));
            if (path != null) {
                TreeUtil.selectPath(myTree, createdPath);
                IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(myTree, true));
            }
        };

        myRemoveAction = button -> {
            myTree.stopEditing();
            if (myTree.getSelectionModel().getSelectionMode() == TreeSelectionModel.SINGLE_TREE_SELECTION) {
                final TreePath path = myTree.getSelectionPath();
                if (path != null) {
                    treeModel.removeNode(path);
                }
            } else {
                final TreePath[] paths = myTree.getSelectionPaths();
                if (paths != null && paths.length > 0) {
                    treeModel.removeNodes(Arrays.asList(paths));
                }
            }
        };
    }

    @Override
    protected @NotNull JComponent getComponent() {
        return myComponent;
    }

    @Override
    protected void updateButtons() {
        getActionsPanel().setEnabled(CommonActionsPanel.Buttons.REMOVE, myTree.getSelectionPath() != null);
    }

    @Override
    public @NotNull ToolbarDecorator setVisibleRowCount(int rowCount) {
        myTree.setVisibleRowCount(rowCount);
        return this;
    }

    @Override
    protected boolean isModelEditable() {
        return myTree.getModel() instanceof EditableModel;
    }

    @Override
    protected void installDnDSupport() {
        RowsDnDSupport.install(myTree, (EditableModel) myTree.getModel());
    }
}
