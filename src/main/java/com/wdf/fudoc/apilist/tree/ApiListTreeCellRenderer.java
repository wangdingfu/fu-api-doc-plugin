package com.wdf.fudoc.apilist.tree;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * API 列表树节点渲染器
 * 自定义树节点的显示样式(图标、颜色、字体等)
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
public class ApiListTreeCellRenderer extends ColoredTreeCellRenderer {

    @Override
    public void customizeCellRenderer(@NotNull JTree tree,
                                       Object value,
                                       boolean selected,
                                       boolean expanded,
                                       boolean leaf,
                                       int row,
                                       boolean hasFocus) {
        if (!(value instanceof ApiTreeNode)) {
            return;
        }

        ApiTreeNode node = (ApiTreeNode) value;

        switch (node.getNodeType()) {
            case ROOT:
                renderRootNode();
                break;
            case MODULE:
                renderModuleNode((ModuleTreeNode) node, expanded);
                break;
            case PACKAGE:
                renderPackageNode((PackageTreeNode) node, expanded);
                break;
            case CONTROLLER:
                renderControllerNode((ControllerTreeNode) node, expanded);
                break;
            case API:
                renderApiNode((ApiItemTreeNode) node);
                break;
            case GROUP:
                renderGroupNode((GroupTreeNode) node, expanded);
                break;
        }
    }

    /**
     * 渲染根节点
     */
    private void renderRootNode() {
        setIcon(AllIcons.Nodes.Module);
        append("所有 API", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
    }

    /**
     * 渲染 Module 节点
     */
    private void renderModuleNode(ModuleTreeNode node, boolean expanded) {
        if (expanded) {
            setIcon(AllIcons.Nodes.ModuleGroup);
        } else {
            setIcon(AllIcons.Nodes.Module);
        }
        append(node.getModuleName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
    }

    /**
     * 渲染 Package 节点
     */
    private void renderPackageNode(PackageTreeNode node, boolean expanded) {
        if (expanded) {
            setIcon(AllIcons.Nodes.Package);
        } else {
            setIcon(AllIcons.Nodes.Package);
        }
        append(node.getPackageName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
    }

    /**
     * 渲染 Controller 节点
     */
    private void renderControllerNode(ControllerTreeNode node, boolean expanded) {
        setIcon(AllIcons.Nodes.Class);
        append(node.getSimpleClassName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
    }

    /**
     * 渲染分组节点
     */
    private void renderGroupNode(GroupTreeNode node, boolean expanded) {
        // 设置图标(展开/折叠)
        if (expanded) {
            setIcon(AllIcons.Nodes.ModuleGroup);
        } else {
            setIcon(AllIcons.Nodes.Module);
        }

        // 显示分组名称
        append(node.getGroup().getGroupName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);

        // 显示 API 数量
        append(" (" + node.getGroup().getItemCount() + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES);
    }

    /**
     * 渲染 API 节点
     */
    private void renderApiNode(ApiItemTreeNode node) {
        RequestType requestType = node.getApiItem().getRequestType();

        // 设置图标
        setIcon(getRequestTypeIcon(requestType));

        // 显示请求类型 (带颜色)
        SimpleTextAttributes typeAttributes = getRequestTypeAttributes(requestType);
        append(requestType.getRequestType(), typeAttributes);

        // 显示 URL
        append("  " + node.getApiItem().getUrl(), SimpleTextAttributes.REGULAR_ATTRIBUTES);

        // 显示标题和方法签名(灰色)
        String rightText = "  " + node.getApiItem().getDisplayText() + "  " + node.getApiItem().getMethodSignature();
        append(rightText, SimpleTextAttributes.GRAYED_ATTRIBUTES);
    }

    /**
     * 根据请求类型获取图标
     */
    private Icon getRequestTypeIcon(RequestType requestType) {
        switch (requestType) {
            case GET:
                return AllIcons.Actions.Download;
            case POST:
                return AllIcons.Actions.Upload;
            case PUT:
                return AllIcons.Actions.Edit;
            case DELETE:
                return AllIcons.Actions.Cancel;
            default:
                return FuDocIcons.HTTP;
        }
    }

    /**
     * 根据请求类型获取文本属性(颜色)
     */
    private SimpleTextAttributes getRequestTypeAttributes(RequestType requestType) {
        switch (requestType) {
            case GET:
                return new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD,
                        new java.awt.Color(0, 128, 0));  // 绿色
            case POST:
                return new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD,
                        new java.awt.Color(255, 140, 0)); // 橙色
            case PUT:
                return new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD,
                        new java.awt.Color(0, 0, 255));   // 蓝色
            case DELETE:
                return new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD,
                        new java.awt.Color(255, 0, 0));   // 红色
            default:
                return SimpleTextAttributes.REGULAR_ATTRIBUTES;
        }
    }
}
