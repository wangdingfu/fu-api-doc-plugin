package com.wdf.fudoc.apilist.pojo;

import com.wdf.fudoc.apilist.constant.GroupType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * API 列表分组对象
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
@Setter
public class ApiListGroup {

    /**
     * 分组名称
     */
    private final String groupName;

    /**
     * 分组类型
     */
    private final GroupType groupType;

    /**
     * 该分组下的 API 列表
     */
    private final List<ApiListItem> items;

    public ApiListGroup(@NotNull String groupName, @NotNull GroupType groupType) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.items = new ArrayList<>();
    }

    /**
     * 添加 API 项
     */
    public void addItem(ApiListItem item) {
        this.items.add(item);
    }

    /**
     * 获取该分组下的 API 数量
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * 获取显示文本 (分组名 + 数量)
     */
    @NotNull
    public String getDisplayText() {
        return groupName + " (" + getItemCount() + ")";
    }
}
