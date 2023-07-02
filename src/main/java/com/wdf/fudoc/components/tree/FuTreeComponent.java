package com.wdf.fudoc.components.tree;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.SimpleTree;
import com.wdf.fudoc.components.tree.node.FuTreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-02 15:45:39
 */
public class FuTreeComponent<T> {

    /**
     * 树
     */
    private final SimpleTree simpleTree;

    private final FuTreeNode<T> root;

    private final DefaultTreeModel model;

    private final FuTreeActionListener<T> listener;

    private JPopupMenu popupMenu;

    private JMenuItem addBrotherNode;

    public FuTreeComponent(Project project, FuTreeNode<T> root, FuTreeActionListener<T> listener) {
        this.root = root;
        this.listener = listener;
        this.simpleTree = new SimpleTree();
        this.model = (DefaultTreeModel) this.simpleTree.getModel();
        model.setRoot(root);
        this.simpleTree.setCellRenderer(new FuTreeCellRenderer());
        initCatalogTree();
        initEvent();
        new TreeSpeedSearch(this.simpleTree);
    }

    /**
     * 返回树形组件（如果配置了监听器 则会渲染上工具栏）
     *
     * @return 树形组件
     */
    public JComponent createUI() {
        return Objects.isNull(listener) ? this.simpleTree : ToolbarDecorator.createDecorator(this.simpleTree)
                .setAddAction(anActionButton -> addNode(true))
                .setRemoveAction(anActionButton -> removeNode())
                .initPosition().createPanel();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        this.simpleTree.addMouseListener(new PopupHandler() {
            @Override
            public void invokePopup(Component comp, int x, int y) {
                showPopupMenu(x, y, createPopupMenu());
            }
        });
    }


    /**
     * 向当前选中的节点添加新节点 如果没有选中则添加到根节点下
     *
     * @param isChild true添加选中节点的子集节点 否则添加兄弟节点
     */
    @SuppressWarnings("all")
    private void addNode(boolean isChild) {
        //新增节点
        FuTreeNode<T> parent = getSelectedNode();
        if (Objects.isNull(parent)) {
            parent = root;
        }
        TreeNode treeNode;
        if (!isChild && Objects.nonNull(treeNode = parent.getParent()) && treeNode instanceof FuTreeNode<?>) {
            parent = (FuTreeNode<T>) treeNode;
        }
        FuTreeNode<T> node = listener.createNode(parent);
        if (Objects.isNull(node)) {
            return;
        }
        this.model.insertNodeInto(node, parent, parent.getChildCount());
    }


    /**
     * 移除当前选中的节点
     */
    private void removeNode() {
        //移除节点
        FuTreeNode<T> parent = getSelectedNode();
        if (Objects.isNull(parent)) {
            return;
        }
        if (listener.removeNode(parent)) {
            this.model.removeNodeFromParent(parent);
        }
    }


    /**
     * 创建右键菜单
     */
    private JPopupMenu createPopupMenu() {
        if (Objects.nonNull(popupMenu)) {
            return popupMenu;
        }
        // 新增子级分类
        JMenuItem addChildNode = new JBMenuItem("新增子级分类", AllIcons.General.Add);
        addChildNode.addActionListener(actionEvent -> addNode(true));

        this.addBrotherNode = new JBMenuItem("新增同级分类", AllIcons.General.Add);
        addBrotherNode.addActionListener(actionEvent -> addNode(false));

        JMenuItem removeNode = new JBMenuItem("删除当前分类", AllIcons.General.Remove);
        removeNode.addActionListener(actionEvent -> removeNode());
        return (popupMenu = generatePopupMenu(addChildNode, addBrotherNode, removeNode));
    }


    /**
     * 构建右键展示的菜单
     *
     * @param items 菜单项
     * @return 右键展示的菜单
     */
    @NotNull
    private JPopupMenu generatePopupMenu(JComponent... items) {
        JBPopupMenu menu = new JBPopupMenu();
        if (items == null) {
            return menu;
        }
        for (int i = 0; i < items.length; i++) {
            JComponent item = items[i];
            if (item != null) {
                if (item instanceof JMenuItem) {
                    ((JMenuItem) item).setMnemonic(i);
                }
                menu.add(item);
            } else {
                menu.addSeparator();
            }
        }
        return menu;
    }

    /**
     * 显示右键菜单
     */
    private void showPopupMenu(int x, int y, @Nullable JPopupMenu menu) {
        if (menu == null) {
            return;
        }
        TreePath path = this.simpleTree.getPathForLocation(x, y);
        if (Objects.isNull(path)) {
            return;
        }
        addBrotherNode.setEnabled(Objects.nonNull(path.getParentPath()));
        this.simpleTree.setSelectionPath(path);
        Rectangle rectangle = this.simpleTree.getUI().getPathBounds(this.simpleTree, path);
        if (rectangle != null && rectangle.contains(x, y)) {
            menu.show(this.simpleTree, x, rectangle.y + rectangle.height);
        }
    }


    @SuppressWarnings("all")
    public FuTreeNode<T> getSelectedNode() {
        Object selectedPathComponent = this.simpleTree.getLastSelectedPathComponent();
        if (Objects.isNull(selectedPathComponent)) {
            return null;
        }
        if (selectedPathComponent instanceof FuTreeNode<?> parent) {
            return (FuTreeNode<T>) parent;
        }
        return null;
    }


    /**
     * 初始化目录树
     */
    private void initCatalogTree() {
        simpleTree.setRootVisible(true);
        simpleTree.setShowsRootHandles(true);
        simpleTree.getEmptyText().clear();
        simpleTree.setBorder(BorderFactory.createEmptyBorder());
        simpleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }


}
