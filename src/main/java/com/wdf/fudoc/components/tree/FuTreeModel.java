package com.wdf.fudoc.components.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author wangdingfu
 * @date 2023-07-02 13:52:30
 */
public class FuTreeModel extends DefaultTreeModel {
    public FuTreeModel(TreeNode root) {
        super(root);
    }
}
