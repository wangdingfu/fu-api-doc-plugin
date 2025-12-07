package com.wdf.fudoc.apilist.tree;

import lombok.Getter;

/**
 * Package 树节点
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public class PackageTreeNode extends ApiTreeNode {

    private final String packageName;

    public PackageTreeNode(String packageName) {
        super(packageName, NodeType.PACKAGE);
        this.packageName = packageName;
    }

    @Override
    public String getDisplayText() {
        return packageName;
    }
}
