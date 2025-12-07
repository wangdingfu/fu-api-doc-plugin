package com.wdf.fudoc.apilist.tree;

import lombok.Getter;

/**
 * Controller 树节点
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public class ControllerTreeNode extends ApiTreeNode {

    private final String className;
    private final String simpleClassName;

    public ControllerTreeNode(String className) {
        super(className, NodeType.CONTROLLER);
        this.className = className;
        this.simpleClassName = extractSimpleClassName(className);
    }

    @Override
    public String getDisplayText() {
        return simpleClassName;
    }

    /**
     * 提取简单类名
     */
    private String extractSimpleClassName(String fullClassName) {
        if (fullClassName == null || fullClassName.isEmpty()) {
            return "Unknown";
        }
        int lastDot = fullClassName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fullClassName.length() - 1) {
            return fullClassName.substring(lastDot + 1);
        }
        return fullClassName;
    }
}
