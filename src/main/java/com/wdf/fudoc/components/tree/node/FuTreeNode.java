package com.wdf.fudoc.components.tree.node;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 树形结构节点对象
 *
 * @author wangdingfu
 * @date 2023-07-02 15:07:47
 */
public class FuTreeNode<T> extends DefaultMutableTreeNode {

    private final T data;

    @Getter
    private final Icon icon;

    public FuTreeNode(T data) {
        this(data, null);
    }

    public FuTreeNode(@NotNull T data, Icon icon) {
        super(data);
        this.icon = icon;
        this.data = data;
    }

    @NotNull
    public T getData() {
        return data;
    }

}
