package com.wdf.fudoc.components.tree;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-01-07 20:33:13
 */
public class FuModuleTreeComponent extends JComponent {


    @Getter
    private final SimpleTree catalogTree;

    @Getter
    private final ModuleNode root;

    private final Map<String, Integer> moduleIndexMap = new ConcurrentHashMap<>();



    public FuModuleTreeComponent(Project project) {
        super();
        FuModuleTreeStructure fuModuleTreeStructure = new FuModuleTreeStructure(project);
        this.root = fuModuleTreeStructure.getRoot();
        StructureTreeModel<FuModuleTreeStructure> treeModel = new StructureTreeModel<>(fuModuleTreeStructure, null, project);
        AsyncTreeModel asyncTreeModel = new AsyncTreeModel(treeModel, project);
        catalogTree = new SimpleTree(asyncTreeModel);
        initCatalogTree();
        addModuleName(this.root, 0);
    }

    private void addModuleName(ModuleNode moduleNode, Integer index) {
        if (Objects.isNull(moduleNode)) {
            return;
        }
        moduleIndexMap.put(moduleNode.getName(), index);
        List<ModuleNode> childList = moduleNode.getChildList();
        if (CollectionUtils.isNotEmpty(childList)) {
            for (ModuleNode node : childList) {
                addModuleName(node, ++index);
            }
        }
    }

    public Integer getModuleIndex(String moduleName){
        return moduleIndexMap.get(moduleName);
    }

    /**
     * 初始化目录树
     */
    private void initCatalogTree() {
        catalogTree.setRootVisible(true);
        catalogTree.setShowsRootHandles(true);
        catalogTree.getEmptyText().clear();
        catalogTree.setBorder(BorderFactory.createEmptyBorder());
        catalogTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }
}
