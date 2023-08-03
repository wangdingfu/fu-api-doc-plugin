package com.wdf.fudoc.components.tree.old;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.ui.treeStructure.SimpleNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-08 21:59:21
 */
public class ApiCategoryTreeNode extends FuTreeNode {

    @Getter
    private final String categoryId;

    @Getter
    private final String categoryName;

    @Getter
    @Setter
    private List<ApiCategoryTreeNode> childList = Lists.newArrayList();

    public ApiCategoryTreeNode(String categoryId, String categoryName, SimpleNode parent) {
        super(parent);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        getTemplatePresentation().setIcon(AllIcons.Nodes.Package);
        getTemplatePresentation().setPresentableText(getName());
    }

    @Override
    public String getName() {
        return this.categoryName;
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return childList.toArray(new ApiCategoryTreeNode[0]);
    }
}
