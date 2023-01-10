package com.wdf.fudoc.apidoc.view.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.strategy.SyncFuDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncStrategyFactory;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.components.tree.ApiCategoryTreeNode;
import com.wdf.fudoc.components.tree.FuTreeComponent;
import com.wdf.fudoc.components.validator.InputExistsValidator;
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
    private ComboBox<ApiProjectDTO> projectNameComboBox;


    /**
     * 选定项目的接口分类下拉框
     */
    private ComboBox<ApiCategoryDTO> categoryNameComboBox;

    /**
     * 选定项目的接口分类树
     */
    private FuTreeComponent<ApiCategoryTreeNode> treeComponent;

    /**
     * 生成的接口文档所处的module
     */
    private final String moduleName;

    private ApiProjectDTO apiProjectDTO;

    public SyncApiCategoryDialog(@Nullable Project project, boolean isCategoryTree, String moduleName, ApiProjectDTO apiProjectDTO) {
        super(project, true);
        this.project = project;
        this.isCategoryTree = isCategoryTree;
        this.moduleName = moduleName;
        this.apiProjectDTO = apiProjectDTO;
        setTitle("为你的接口选择一个分类");
        this.rootPanel.setPreferredSize(new Dimension(400, 100));
        initRoot();
        init();
    }


    private void initRoot() {
        //填充项目名称下拉框
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        List<ApiProjectDTO> projectConfigList = enableConfigData.getProjectConfigList(this.moduleName);
        this.projectNameComboBox = new ComboBox<>(projectConfigList.toArray(new ApiProjectDTO[0]));
        if (Objects.isNull(this.apiProjectDTO)) {
            this.apiProjectDTO = projectConfigList.get(0);
        }
        this.projectNameComboBox.setSelectedItem(this.apiProjectDTO);
        this.projectPanel.add(new JLabel("YApi项目名称:"), BorderLayout.WEST);
        this.projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        LinkLabel<String> projectLinkLabel = new LinkLabel<>("创建一个项目", null, (aSource, aLinkData) -> {
            List<String> projectNameList = ObjectUtils.listToList(projectConfigList, ApiProjectDTO::getProjectName);
            String value = Messages.showInputDialog(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN), FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN_TITLE), Messages.getQuestionIcon(), StringUtils.EMPTY, new InputExistsValidator(projectNameList));
            //请求创建项目
            this.projectNameComboBox.addItem(new ApiProjectDTO());
        });
        projectLinkLabel.setEnabled(false);
        projectLinkLabel.setBorder(JBUI.Borders.emptyLeft(10));
        this.projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        this.projectPanel.add(projectLinkLabel, BorderLayout.EAST);
        this.projectPanel.setBorder(JBUI.Borders.empty(10));
        this.categoryPanel.setBorder(JBUI.Borders.empty(10, 10, 20, 10));
        //项目下拉框切换项目事件
        this.projectNameComboBox.addItemListener(e -> {
            apiProjectDTO = (ApiProjectDTO) e.getItem();
            //重置api分类下拉框
            resetCategory(enableConfigData);
        });
        //渲染接口分类
        resetCategory(enableConfigData);
    }


    /**
     * 获取当前选中的项目
     */
    public String getSelectProjectName() {
        return this.projectNameComboBox.getSelectedItem() + "";
    }

    public ApiProjectDTO getSelectCategory() {
        if (isCategoryTree) {
            TreePath selectionPath = treeComponent.getCatalogTree().getSelectionPath();
            Object lastPathComponent;
            if (Objects.nonNull(selectionPath) && Objects.nonNull(lastPathComponent = selectionPath.getLastPathComponent())) {
                apiProjectDTO.setSelectCategory(new ApiCategoryDTO("",lastPathComponent.toString()));
                return apiProjectDTO;
            }
        } else {
            apiProjectDTO.setSelectCategory((ApiCategoryDTO) categoryNameComboBox.getSelectedItem());
            return apiProjectDTO;
        }
        return null;
    }


    /**
     * 重置分类
     *
     * @param enableConfigData 第三方接口文档配置
     */
    private void resetCategory(BaseSyncConfigData enableConfigData) {
        List<ApiCategoryDTO> apiCategoryList = apiProjectDTO.getApiCategoryList();
        if(CollectionUtils.isEmpty(apiCategoryList)){
            apiCategoryList = listCategory(enableConfigData);
            apiProjectDTO.setApiCategoryList(apiCategoryList);
        }
        if (isCategoryTree) {
            //设置分类树
            this.treeComponent = new FuTreeComponent<>(project, buildCategoryTree(null, this.apiProjectDTO.getProjectName(), null, apiCategoryList));
        } else {
            //设置分类下拉框
            this.categoryNameComboBox = new ComboBox<>(apiCategoryList.toArray(new ApiCategoryDTO[0]));
        }
        this.categoryPanel.add(new JLabel("YApi接口分类:"), BorderLayout.WEST);
        this.categoryPanel.add(isCategoryTree ? this.treeComponent.getCatalogTree() : this.categoryNameComboBox, BorderLayout.CENTER);
        if (!isCategoryTree) {
            List<String> categoryNameList = ObjectUtils.listToList(apiCategoryList, ApiCategoryDTO::getCategoryName);
            LinkLabel<String> linkLabel = new LinkLabel<>("创建一个分类", null, (aSource, aLinkData) -> {
                String value = Messages.showInputDialog(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN), FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN_TITLE), Messages.getQuestionIcon(), StringUtils.EMPTY, new InputExistsValidator(categoryNameList));
                categoryNameComboBox.addItem(new ApiCategoryDTO("",value));
            });
            linkLabel.setBorder(JBUI.Borders.emptyLeft(10));
            this.categoryPanel.add(linkLabel, BorderLayout.EAST);
        }
    }


    private List<ApiCategoryDTO> listCategory(BaseSyncConfigData enableConfigData){
        SyncFuDocStrategy service = SyncStrategyFactory.getInstance();
        if (Objects.isNull(service)) {
            return Lists.newArrayList();
        }
        return service.categoryList(this.apiProjectDTO, enableConfigData);
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


    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }

}
