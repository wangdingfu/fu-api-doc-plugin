package com.wdf.fudoc.apilist.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;
import com.wdf.fudoc.apilist.constant.GroupType;
import com.wdf.fudoc.apilist.pojo.ApiListGroup;
import com.wdf.fudoc.apilist.pojo.ApiListItem;
import com.wdf.fudoc.apilist.service.ApiListCollector;
import com.wdf.fudoc.apilist.strategy.ApiGroupStrategy;
import com.wdf.fudoc.apilist.strategy.ModuleGroupStrategy;
import com.wdf.fudoc.apilist.strategy.PrefixGroupStrategy;
import com.wdf.fudoc.apilist.tree.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * API 列表工具窗口主面板
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Slf4j
public class ApiListToolWindow extends SimpleToolWindowPanel {

    private final Project project;

    @Getter
    private final Tree tree;

    private final ApiTreeNode rootNode;
    private final DefaultTreeModel treeModel;

    private SearchTextField searchField;
    private GroupType currentGroupType = GroupType.MODULE;

    // 缓存完整的 API 列表,用于搜索过滤
    private List<ApiListItem> cachedApiList = new ArrayList<>();

    // 防抖定时器
    private javax.swing.Timer searchTimer;

    public ApiListToolWindow(@NotNull Project project) {
        super(true, true);
        this.project = project;

        // 创建根节点和树模型
        this.rootNode = new ApiTreeNode(null, ApiTreeNode.NodeType.ROOT) {
            @Override
            public String getDisplayText() {
                return "所有 API";
            }
        };
        this.treeModel = new DefaultTreeModel(rootNode);
        this.tree = new Tree(treeModel);

        // 初始化 UI
        initUI();

        // 异步加载 API 数据
        loadApis();
    }

    /**
     * 初始化 UI 组件
     */
    private void initUI() {
        // 设置树的渲染器
        tree.setCellRenderer(new ApiListTreeCellRenderer());

        // 设置树的根节点可见
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        // 启用快速搜索
        new TreeSpeedSearch(tree, treePath -> {
            Object node = treePath.getLastPathComponent();
            if (node instanceof ApiTreeNode) {
                return ((ApiTreeNode) node).getDisplayText();
            }
            return "";
        });

        // 添加双击监听器(跳转到源码)
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClick();
                }
            }
        });

        // 添加右键菜单
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
        });

        // 创建工具栏
        JPanel toolbarPanel = createToolbarPanel();

        // 创建滚动面板
        JBScrollPane scrollPane = new JBScrollPane(tree);

        // 布局
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(toolbarPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        setContent(contentPanel);
    }

    /**
     * 创建工具栏面板
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 创建搜索框
        searchField = new SearchTextField(true);

        // 初始化防抖定时器 (300ms 延迟)
        searchTimer = new javax.swing.Timer(300, e -> performFilter());
        searchTimer.setRepeats(false);

        searchField.addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                scheduleFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                scheduleFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                scheduleFilter();
            }
        });

        // 创建操作按钮组
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        // 刷新按钮
        actionGroup.add(new AnAction("刷新", "重新加载所有 API", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                loadApis();
            }
        });

        // 分组方式切换按钮
        actionGroup.add(new AnAction("按模块分组", "按 Module 分组 API", AllIcons.Nodes.Module) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                switchGroupType(GroupType.MODULE);
            }
        });

        actionGroup.add(new AnAction("按前缀分组", "按 URL 前缀分组 API", AllIcons.Nodes.Folder) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                switchGroupType(GroupType.PREFIX);
            }
        });

        // 展开/折叠按钮
        actionGroup.add(new AnAction("展开全部", "展开所有分组", AllIcons.Actions.Expandall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                TreeUtil.expandAll(tree);
            }
        });

        actionGroup.add(new AnAction("折叠全部", "折叠所有分组", AllIcons.Actions.Collapseall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                TreeUtil.collapseAll(tree, 0);
            }
        });

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("ApiListToolbar", actionGroup, true);
        toolbar.setTargetComponent(this);

        panel.add(searchField, BorderLayout.NORTH);
        panel.add(toolbar.getComponent(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * 加载 API 列表
     */
    private void loadApis() {
        // 清空当前树
        rootNode.removeAllChildren();
        treeModel.reload();

        // 在后台线程收集 API
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                // 收集 API
                ApiListCollector collector = ApiListCollector.getInstance(project);
                List<ApiListItem> apiList = collector.collectAllApis();

                // 缓存数据
                cachedApiList = apiList;

                // 在 EDT 线程更新 UI
                ApplicationManager.getApplication().invokeLater(() -> {
                    buildTree(apiList);
                });
            } catch (Exception e) {
                log.error("加载 API 列表失败", e);
            }
        });
    }

    /**
     * 构建树 (根据当前分组类型)
     */
    private void buildTree(List<ApiListItem> apiList) {
        if (currentGroupType == GroupType.MODULE) {
            buildModuleTree(apiList);
        } else {
            buildPrefixTree(apiList);
        }
    }

    /**
     * 构建 Module 层级树 (VSCode 风格的包路径展示)
     * 规则：叶子包合并（中间只有一条路径的包节点合并显示）
     */
    private void buildModuleTree(List<ApiListItem> apiList) {
        rootNode.removeAllChildren();

        // 1. 按 Module 分组
        Map<String, List<ApiListItem>> moduleMap = apiList.stream()
                .collect(Collectors.groupingBy(
                        api -> api.getModuleName() != null ? api.getModuleName() : "Unknown",
                        Collectors.toList()
                ));

        // 2. 为每个 Module 构建包树
        moduleMap.forEach((moduleName, moduleApis) -> {
            ModuleTreeNode moduleNode = new ModuleTreeNode(moduleName);
            rootNode.add(moduleNode);

            // 3. 按包名分组所有 Controller
            Map<String, List<ApiListItem>> packageMap = moduleApis.stream()
                    .collect(Collectors.groupingBy(
                            this::extractPackageName,
                            Collectors.toList()
                    ));

            // 4. 构建包树（带路径压缩）
            buildPackageTree(moduleNode, packageMap);
        });

        // 刷新树并展开到 Controller 层（不展开 API 列表）
        treeModel.reload();
        expandToControllerLevel();
    }

    /**
     * 展开树到 Controller 层级（不展开 API 列表）
     * 递归展开所有非 API 节点
     */
    private void expandToControllerLevel() {
        // 从根节点开始递归展开
        expandNodeExceptApiLevel(new TreePath(rootNode));
    }

    /**
     * 递归展开节点，但不展开 Controller 节点（即不显示 API 列表）
     */
    private void expandNodeExceptApiLevel(TreePath path) {
        Object node = path.getLastPathComponent();
        if (!(node instanceof ApiTreeNode)) {
            return;
        }

        ApiTreeNode apiNode = (ApiTreeNode) node;

        // 如果是 Controller 节点，不展开（保持 API 列表折叠）
        if (apiNode.getNodeType() == ApiTreeNode.NodeType.CONTROLLER) {
            return;
        }

        // 展开当前节点
        tree.expandPath(path);

        // 递归展开所有子节点
        int childCount = apiNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Object child = apiNode.getChildAt(i);
            TreePath childPath = path.pathByAddingChild(child);
            expandNodeExceptApiLevel(childPath);
        }
    }

    /**
     * 构建包树（VSCode 风格：叶子包合并）
     *
     * 新算法：
     * 1. 构建完整的包树结构（Trie树）
     * 2. 标记哪些包节点包含 Controller
     * 3. 应用路径压缩：如果某节点只有一个子节点且不包含 Controller，则与子节点合并
     */
    private void buildPackageTree(ModuleTreeNode moduleNode, Map<String, List<ApiListItem>> packageMap) {
        // 第一步：构建包树节点结构
        PackageTreeBuilder treeBuilder = new PackageTreeBuilder();

        // 添加所有包路径和对应的 API
        packageMap.forEach(treeBuilder::addPackage);

        // 第二步：构建压缩后的树，并添加到 moduleNode
        treeBuilder.buildCompressedTree(moduleNode);
    }

    /**
     * 包树构建器（内部类）
     * 负责构建 VSCode 风格的包树结构
     */
    private class PackageTreeBuilder {
        // 包节点映射：完整包名 -> 包数据
        private final Map<String, PackageNodeData> packageDataMap = new java.util.HashMap<>();

        /**
         * 包节点数据
         */
        private class PackageNodeData {
            String fullPackageName;
            List<ApiListItem> apis = new ArrayList<>();  // 此包下的 API（如果有）
            Set<String> childPackages = new java.util.HashSet<>();  // 直接子包的完整名称

            PackageNodeData(String fullPackageName) {
                this.fullPackageName = fullPackageName;
            }
        }

        /**
         * 添加包及其 API
         */
        void addPackage(String fullPackageName, List<ApiListItem> apis) {
            // 创建或获取当前包节点
            PackageNodeData currentData = packageDataMap.computeIfAbsent(
                fullPackageName,
                PackageNodeData::new
            );
            currentData.apis.addAll(apis);

            // 创建所有父包节点（如果不存在）
            String[] parts = fullPackageName.split("\\.");
            for (int i = 0; i < parts.length - 1; i++) {
                String parentPackage = String.join(".", java.util.Arrays.copyOfRange(parts, 0, i + 1));
                String childPackage = String.join(".", java.util.Arrays.copyOfRange(parts, 0, i + 2));

                PackageNodeData parentData = packageDataMap.computeIfAbsent(
                    parentPackage,
                    PackageNodeData::new
                );
                parentData.childPackages.add(childPackage);
            }
        }

        /**
         * 构建压缩后的树
         */
        void buildCompressedTree(ModuleTreeNode moduleNode) {
            // 找出所有根包（没有父包的包）
            Set<String> rootPackages = findRootPackages();

            // 为每个根包构建子树（父路径为空）
            for (String rootPackage : rootPackages) {
                buildSubTree(rootPackage, "", moduleNode);
            }
        }

        /**
         * 找出所有根包
         */
        private Set<String> findRootPackages() {
            Set<String> allPackages = new java.util.HashSet<>(packageDataMap.keySet());
            Set<String> nonRootPackages = new java.util.HashSet<>();

            // 移除所有是其他包子包的包
            for (PackageNodeData data : packageDataMap.values()) {
                nonRootPackages.addAll(data.childPackages);
            }

            allPackages.removeAll(nonRootPackages);
            return allPackages;
        }

        /**
         * 递归构建子树（带路径压缩）
         *
         * @param packageName 当前包的完整名称
         * @param parentPackagePath 父包的完整路径（用于计算显示名称）
         * @param parentNode 父树节点
         */
        private void buildSubTree(String packageName, String parentPackagePath, javax.swing.tree.DefaultMutableTreeNode parentNode) {
            PackageNodeData data = packageDataMap.get(packageName);
            if (data == null) {
                return;
            }

            // 查找压缩路径：如果只有一个子包且当前包没有 Controller，继续合并
            String compressedPackageName = findCompressedPath(packageName);
            PackageNodeData compressedData = packageDataMap.get(compressedPackageName);

            // 计算显示名称（相对于父包的路径）
            String displayName;
            if (parentPackagePath.isEmpty()) {
                // 根包：显示完整压缩路径
                displayName = compressedPackageName;
            } else {
                // 子包：只显示相对于父包的路径
                if (compressedPackageName.startsWith(parentPackagePath + ".")) {
                    displayName = compressedPackageName.substring(parentPackagePath.length() + 1);
                } else {
                    displayName = compressedPackageName;
                }
            }

            // 创建包节点
            PackageTreeNode packageNode = new PackageTreeNode(displayName);
            parentNode.add(packageNode);

            // 添加此包下的所有 Controller
            if (compressedData != null && !compressedData.apis.isEmpty()) {
                Map<String, List<ApiListItem>> controllerMap = compressedData.apis.stream()
                        .collect(Collectors.groupingBy(ApiListItem::getClassName));

                controllerMap.forEach((className, controllerApis) -> {
                    ControllerTreeNode controllerNode = new ControllerTreeNode(className);
                    packageNode.add(controllerNode);

                    // 添加 API 节点
                    controllerApis.forEach(apiItem -> {
                        ApiItemTreeNode apiNode = new ApiItemTreeNode(apiItem);
                        controllerNode.add(apiNode);
                    });
                });
            }

            // 递归构建子包（使用压缩后的完整路径作为新的父路径）
            if (compressedData != null) {
                for (String childPackage : compressedData.childPackages) {
                    buildSubTree(childPackage, compressedPackageName, packageNode);
                }
            }
        }

        /**
         * 查找压缩路径
         * 如果当前包只有一个子包且没有 Controller，则与子包合并
         */
        private String findCompressedPath(String packageName) {
            String current = packageName;

            while (true) {
                PackageNodeData data = packageDataMap.get(current);
                if (data == null) {
                    break;
                }

                // 如果有 API 或有多个子包，停止压缩
                if (!data.apis.isEmpty() || data.childPackages.size() != 1) {
                    break;
                }

                // 继续压缩到唯一的子包
                current = data.childPackages.iterator().next();
            }

            return current;
        }
    }

    /**
     * 构建 Prefix 扁平树 (两级: Prefix → API)
     */
    private void buildPrefixTree(List<ApiListItem> apiList) {
        rootNode.removeAllChildren();

        // 按 URL 前缀分组
        ApiGroupStrategy strategy = new PrefixGroupStrategy();
        List<ApiListGroup> groups = strategy.group(apiList);

        for (ApiListGroup group : groups) {
            // 创建分组节点
            GroupTreeNode groupNode = new GroupTreeNode(group);
            rootNode.add(groupNode);

            // 添加 API 节点
            for (ApiListItem apiItem : group.getItems()) {
                ApiItemTreeNode apiNode = new ApiItemTreeNode(apiItem);
                groupNode.add(apiNode);
            }
        }

        // 刷新树并展开到分组层（不展开 API 列表）
        treeModel.reload();
        expandToControllerLevel();
    }

    /**
     * 提取包名 (不含类名)
     * 例如: "com.example.controller.UserController" -> "com.example.controller"
     */
    private String extractPackageName(ApiListItem api) {
        String className = api.getClassName();
        if (className == null || className.isEmpty()) {
            return "default";
        }
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            return className.substring(0, lastDot);
        }
        return "default";
    }

    /**
     * 切换分组方式
     */
    private void switchGroupType(GroupType groupType) {
        if (this.currentGroupType != groupType) {
            this.currentGroupType = groupType;
            // 重新应用当前搜索过滤
            performFilter();
        }
    }

    /**
     * 获取分组策略
     */
    private ApiGroupStrategy getGroupStrategy(GroupType groupType) {
        switch (groupType) {
            case PREFIX:
                return new PrefixGroupStrategy();
            case MODULE:
            default:
                return new ModuleGroupStrategy();
        }
    }

    /**
     * 调度过滤任务 (防抖)
     */
    private void scheduleFilter() {
        if (searchTimer.isRunning()) {
            searchTimer.restart();
        } else {
            searchTimer.start();
        }
    }

    /**
     * 执行过滤
     */
    private void performFilter() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            // 清空搜索,显示全部数据
            buildTree(cachedApiList);
            return;
        }

        // 过滤 API 列表
        List<ApiListItem> filteredList = cachedApiList.stream()
                .filter(api -> matchesKeyword(api, keyword))
                .collect(Collectors.toList());

        // 重建树
        buildTree(filteredList);

        // 如果有结果,展开所有节点以便查看
        if (!filteredList.isEmpty()) {
            TreeUtil.expandAll(tree);
        } else {
            // 无结果时也要刷新树模型
            treeModel.reload();
        }
    }

    /**
     * 判断 API 是否匹配关键词
     * 匹配范围: URL、标题、类名、方法名
     */
    private boolean matchesKeyword(ApiListItem api, String keyword) {
        // 1. 匹配 URL
        if (api.getUrl() != null && api.getUrl().toLowerCase().contains(keyword)) {
            return true;
        }

        // 2. 匹配标题
        if (api.getTitle() != null && api.getTitle().toLowerCase().contains(keyword)) {
            return true;
        }

        // 3. 匹配类名
        if (api.getClassName() != null && api.getClassName().toLowerCase().contains(keyword)) {
            return true;
        }

        // 4. 匹配方法名
        if (api.getPsiMethod() != null && api.getPsiMethod().getName().toLowerCase().contains(keyword)) {
            return true;
        }

        // 5. 匹配请求类型
        return api.getRequestType() != null && api.getRequestType().getRequestType().toLowerCase().contains(keyword);
    }

    /**
     * 处理双击事件(跳转到源码)
     */
    private void handleDoubleClick() {
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }

        Object node = path.getLastPathComponent();
        if (node instanceof ApiItemTreeNode) {
            ApiItemTreeNode apiNode = (ApiItemTreeNode) node;
            navigateToSource(apiNode.getApiItem());
        }
    }

    /**
     * 显示右键菜单
     */
    private void showContextMenu(java.awt.event.MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return;
        }

        tree.setSelectionPath(path);
        Object node = path.getLastPathComponent();

        if (node instanceof ApiItemTreeNode) {
            ApiItemTreeNode apiNode = (ApiItemTreeNode) node;
            JPopupMenu menu = createApiContextMenu(apiNode.getApiItem());
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * 创建 API 节点的右键菜单
     */
    private JPopupMenu createApiContextMenu(ApiListItem apiItem) {
        JPopupMenu menu = new JPopupMenu();

        // 跳转到源码
        JMenuItem navigateItem = new JMenuItem("跳转到源码", AllIcons.Actions.EditSource);
        navigateItem.addActionListener(e -> navigateToSource(apiItem));
        menu.add(navigateItem);

        menu.addSeparator();

        // 复制 URL
        JMenuItem copyUrlItem = new JMenuItem("复制 URL", AllIcons.Actions.Copy);
        copyUrlItem.addActionListener(e -> copyToClipboard(apiItem.getUrl()));
        menu.add(copyUrlItem);

        // 复制完整路径
        String fullUrl = apiItem.getUrl();
        JMenuItem copyFullUrlItem = new JMenuItem("复制完整 URL", AllIcons.Actions.Copy);
        copyFullUrlItem.addActionListener(e -> copyToClipboard(fullUrl));
        menu.add(copyFullUrlItem);

        return menu;
    }

    /**
     * 跳转到源码
     */
    private void navigateToSource(ApiListItem apiItem) {
        ApplicationManager.getApplication().invokeLater(() -> {
            apiItem.getPsiMethod().navigate(true);
        });
    }

    /**
     * 复制到剪贴板
     */
    private void copyToClipboard(String text) {
        java.awt.Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(text), null);
    }
}
