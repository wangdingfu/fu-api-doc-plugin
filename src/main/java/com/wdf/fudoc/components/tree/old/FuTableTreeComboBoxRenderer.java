package com.wdf.fudoc.components.tree.old;

import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-07 22:24:29
 */
public class FuTableTreeComboBoxRenderer extends DefaultListCellRenderer {

    private final SimpleTree simpleTree;

    public FuTableTreeComboBoxRenderer(SimpleTree simpleTree) {
        this.simpleTree = simpleTree;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null && Objects.nonNull(simpleTree)) {
            TreePath[] path = (TreePath[]) value;
            TreeNode node = (TreeNode) path[0].getLastPathComponent();
            value = node;
            TreeCellRenderer r = simpleTree.getCellRenderer();
            return r.getTreeCellRendererComponent(simpleTree, value, isSelected, false, node.isLeaf(), index,
                    cellHasFocus);
        }
        return super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
    }
}
