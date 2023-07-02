package com.wdf.fudoc.components.tree.old;

import com.intellij.ui.treeStructure.SimpleTreeStructure;
import org.jetbrains.annotations.NotNull;


/**
 * @author wangdingfu
 * @date 2023-01-08 22:03:51
 */
public class FuTreeStructure<T extends FuTreeNode> extends SimpleTreeStructure {
    /**
     * 根module
     */
    private final T root;

    public FuTreeStructure(T root) {
        this.root = root;
    }


    @Override
    public @NotNull Object getRootElement() {
        return root;
    }

    public T getRoot() {
        return root;
    }
}
