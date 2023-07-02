package com.wdf.fudoc.components.tree.old;

import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;

/**
 * 树形节点
 *
 * @author wangdingfu
 * @date 2023-01-08 21:57:50
 */
public abstract class FuTreeNode extends CachingSimpleNode {


    protected FuTreeNode(SimpleNode parent) {
        super(parent);
    }
}
