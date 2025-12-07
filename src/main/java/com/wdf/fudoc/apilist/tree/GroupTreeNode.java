package com.wdf.fudoc.apilist.tree;

import com.wdf.fudoc.apilist.pojo.ApiListGroup;
import lombok.Getter;

/**
 * API 分组树节点
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public class GroupTreeNode extends ApiTreeNode {

    private final ApiListGroup group;

    public GroupTreeNode(ApiListGroup group) {
        super(group, NodeType.GROUP);
        this.group = group;
    }

    @Override
    public String getDisplayText() {
        return group.getDisplayText();
    }
}
