package com.wdf.fudoc.apilist.tree;

import com.wdf.fudoc.apilist.pojo.ApiListItem;
import lombok.Getter;

/**
 * API 树节点
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public class ApiItemTreeNode extends ApiTreeNode {

    private final ApiListItem apiItem;

    public ApiItemTreeNode(ApiListItem apiItem) {
        super(apiItem, NodeType.API);
        this.apiItem = apiItem;
    }

    @Override
    public String getDisplayText() {
        return apiItem.getRequestType().getRequestType() + "  " + apiItem.getUrl();
    }

    /**
     * 获取右侧显示文本 (标题 + 方法签名)
     */
    public String getRightText() {
        return apiItem.getDisplayText() + "  " + apiItem.getMethodSignature();
    }
}
