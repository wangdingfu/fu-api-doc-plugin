package com.wdf.fudoc.apilist.tree;

import lombok.Getter;

/**
 * Module 树节点
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public class ModuleTreeNode extends ApiTreeNode {

    private final String moduleName;

    public ModuleTreeNode(String moduleName) {
        super(moduleName, NodeType.MODULE);
        this.moduleName = moduleName;
    }

    @Override
    public String getDisplayText() {
        return moduleName;
    }
}
