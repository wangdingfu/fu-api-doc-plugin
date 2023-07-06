package com.wdf.fudoc.components.tree;

import com.wdf.fudoc.components.tree.node.FuTreeNode;

/**
 * @author wangdingfu
 * @date 2023-07-02 16:04:50
 */
public interface FuTreeActionListener<T> {

    /**
     * 创建一个新节点
     */
    FuTreeNode<T> createNode(FuTreeNode<T> parent);


    /**
     * 移除节点
     *
     * @param parent 父节点
     */
    boolean removeNode(FuTreeNode<T> parent);

}
