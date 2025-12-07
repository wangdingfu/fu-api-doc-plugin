package com.wdf.fudoc.apilist.tree;

import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * API 列表树节点基类
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public abstract class ApiTreeNode extends DefaultMutableTreeNode {

    /**
     * 节点类型
     */
    public enum NodeType {
        ROOT,       // 根节点
        MODULE,     // Module 节点
        PACKAGE,    // Package 节点
        CONTROLLER, // Controller 节点
        GROUP,      // 通用分组节点(用于 Prefix 分组等)
        API         // API 节点
    }

    private final NodeType nodeType;

    public ApiTreeNode(Object userObject, NodeType nodeType) {
        super(userObject);
        this.nodeType = nodeType;
    }

    /**
     * 获取节点显示文本
     */
    public abstract String getDisplayText();
}
