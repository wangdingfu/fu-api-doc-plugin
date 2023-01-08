package com.wdf.fudoc.components.tree;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.tree.TreePath;
import java.util.List;

/**
 * java module树
 *
 * @author wangdingfu
 * @date 2023-01-07 19:39:28
 */
public class ModuleNode extends CachingSimpleNode {

    /**
     * module名称
     */
    @Getter
    private String moduleName;

    @Getter
    private List<ModuleNode> childList = Lists.newArrayList();

    protected ModuleNode(String moduleName, SimpleNode parent) {
        super(parent);
        getTemplatePresentation().setIcon(AllIcons.Nodes.Module);
        getTemplatePresentation().setPresentableText(getName());
        this.moduleName = moduleName;
    }


    @Override
    public @NlsSafe String getName() {
        return moduleName;
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return childList.toArray(new ModuleNode[0]);
    }
}
