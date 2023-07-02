package com.wdf.fudoc.components.tree;

import com.intellij.ui.ColoredTreeCellRenderer;
import com.wdf.fudoc.components.tree.node.FuTreeNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * 树形组件节点渲染
 *
 * @author wangdingfu
 * @date 2023-07-02 15:53:41
 */
public class FuTreeCellRenderer extends ColoredTreeCellRenderer {


    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof FuTreeNode<?> fuTreeNode) {
            Object data = fuTreeNode.getData();
            Icon icon = fuTreeNode.getIcon();
            if (Objects.nonNull(icon)) {
                setIcon(icon);
            }
            append(data.toString());
        } else {
            append(value.toString());
        }
    }

}
