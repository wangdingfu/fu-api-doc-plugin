package com.wdf.fudoc.apidoc.view.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.strategy.SyncFuDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncStrategyFactory;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.components.tree.ApiCategoryTreeNode;
import com.wdf.fudoc.components.tree.FuTreeComponent;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-08 22:27:18
 */
public class SyncApiCategoryDialog extends DialogWrapper {
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JPanel projectPanel;
    private JPanel categoryPanel;

    private final Project project;


    /**
     * true时 分类为树结构 false时 树为下拉框
     */
    private final boolean isCategoryTree;

    /**
     * 项目名称下拉框
     */
    private ComboBox<String> projectNameComboBox;


    /**
     * 选定项目的接口分类下拉框
     */
    private ComboBox<String> categoryNameComboBox;

    /**
     * 选定项目的接口分类树
     */
    private FuTreeComponent<ApiCategoryTreeNode> treeComponent;

    /**
     * 生成的接口文档所处的module
     */
    private final String moduleName;

    public SyncApiCategoryDialog(@Nullable Project project, boolean isCategoryTree, String moduleName) {
        super(project, true);
        this.project = project;
        this.isCategoryTree = isCategoryTree;
        this.moduleName = moduleName;
        setTitle("请选择需要将接口同步到哪一个分类下");
        initRoot();
        init();
    }


    private void initRoot() {
        //填充项目名称下拉框
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        List<String> projectNameList = enableConfigData.getProjectNameList(this.moduleName);
        this.projectNameComboBox = new ComboBox<>(projectNameList.toArray(new String[0]));
        String projectName = CollectionUtils.isNotEmpty(projectNameList) ? projectNameList.get(0) : StringUtils.EMPTY;
        this.projectNameComboBox.setSelectedItem(projectName);
        this.projectPanel.add(new JLabel("YApi项目名称:"), BorderLayout.WEST);
        this.projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        //项目下拉框切换项目事件
        this.projectNameComboBox.addItemListener(e -> resetCategory(e.getItem() + "", enableConfigData));
        //渲染接口分类
        resetCategory(projectName, enableConfigData);
    }


    /**
     * 获取当前选中的项目
     */
    public String getSelectProjectName() {
        return this.projectNameComboBox.getSelectedItem() + "";
    }

    private ApiCategoryDTO getSelectCategory() {
        if (isCategoryTree) {
            TreePath selectionPath = treeComponent.getCatalogTree().getSelectionPath();
            Object lastPathComponent;
            if (Objects.nonNull(selectionPath) && Objects.nonNull(lastPathComponent = selectionPath.getLastPathComponent())) {
                return new ApiCategoryDTO("", lastPathComponent.toString());
            }
        } else {
            return new ApiCategoryDTO("", categoryNameComboBox.getSelectedItem() + "");
        }
        return null;
    }


    /**
     * 重置分类
     *
     * @param projectName      项目名称
     * @param enableConfigData 第三方接口文档配置
     */
    private void resetCategory(String projectName, BaseSyncConfigData enableConfigData) {
        SyncFuDocStrategy service = SyncStrategyFactory.getInstance();
        if (Objects.isNull(service)) {
            return;
        }
        List<ApiCategoryDTO> categoryList = service.categoryList(projectName, enableConfigData);
        if (isCategoryTree) {
            //设置分类树
            this.treeComponent = new FuTreeComponent<>(project, buildCategoryTree(null, projectName, null, categoryList));
        } else {
            //设置分类下拉框
            this.categoryNameComboBox = new ComboBox<>(ObjectUtils.listToList(categoryList, ApiCategoryDTO::getCategoryName).toArray(new String[0]));
        }
        resetCategory();
    }


    private ApiCategoryTreeNode buildCategoryTree(String categoryId, String categoryName, ApiCategoryTreeNode parent, java.util.List<ApiCategoryDTO> categoryDTOList) {
        ApiCategoryTreeNode treeNode = new ApiCategoryTreeNode(categoryId, categoryName, parent);
        if (CollectionUtils.isNotEmpty(categoryDTOList)) {
            List<ApiCategoryTreeNode> childList = Lists.newArrayList();
            for (ApiCategoryDTO apiCategoryDTO : categoryDTOList) {
                childList.add(buildCategoryTree(apiCategoryDTO.getCategoryId(), apiCategoryDTO.getCategoryName(), treeNode, apiCategoryDTO.getApiCategoryList()));
            }
            treeNode.setChildList(childList);
        }
        return treeNode;
    }


    private void resetCategory() {
        this.categoryPanel.removeAll();
        this.categoryPanel.repaint();
        this.categoryPanel.add(new JLabel("YApi接口分类:"), BorderLayout.WEST);
        this.categoryPanel.add(isCategoryTree ? this.treeComponent.getCatalogTree() : this.categoryNameComboBox, BorderLayout.CENTER);
        this.categoryPanel.revalidate();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }

}
