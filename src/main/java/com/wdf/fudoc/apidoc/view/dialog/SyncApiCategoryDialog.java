package com.wdf.fudoc.apidoc.view.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidatorEx;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * 选择接口分类弹框
 *
 * @author wangdingfu
 * @date 2023-01-08 22:27:18
 */
@Slf4j
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

    /**
     * 当前选中的项目
     */
    @Getter
    private ApiProjectDTO apiProjectDTO;

    /**
     * 同步策略接口
     */
    private SyncFuDocStrategy strategy;

    /**
     * 第三方接口文档系统配置数据
     */
    private BaseSyncConfigData configData;


    private static final String TITLE = FuDocMessageBundle.message(MessageConstants.SYNC_API_TITLE);
    private static final String PROJECT_LABEL = FuDocMessageBundle.message(MessageConstants.SYNC_API_PROJECT_LABEL);
    private static final String CATEGORY_LABEL = FuDocMessageBundle.message(MessageConstants.SYNC_API_CATEGORY_LABEL);
    private static final String CREATE_PROJECT = FuDocMessageBundle.message(MessageConstants.SYNC_API_CREATE_PROJECT);
    private static final String CREATE_PROJECT_TITLE = FuDocMessageBundle.message(MessageConstants.SYNC_API_CREATE_PROJECT_TITLE);
    private static final String CREATE_CATEGORY = FuDocMessageBundle.message(MessageConstants.SYNC_API_CREATE_CATEGORY);
    private static final String CREATE_CATEGORY_TITLE = FuDocMessageBundle.message(MessageConstants.SYNC_API_CREATE_CATEGORY_TITLE);


    public SyncApiCategoryDialog(@Nullable Project project, boolean isCategoryTree, String moduleName, ApiProjectDTO apiProjectDTO) {
        super(project, true);
        this.project = project;
        this.isCategoryTree = isCategoryTree;
        this.moduleName = moduleName;
        this.apiProjectDTO = apiProjectDTO;
        setTitle(TITLE);
        this.rootPanel.setPreferredSize(new Dimension(400, 100));
        initRoot();
        init();
    }


    /**
     * 初始化当前弹框
     */
    private void initRoot() {
        //填充项目名称下拉框
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        this.configData = settingData.getEnableConfigData();
        //获取
        this.strategy = SyncStrategyFactory.getInstance(settingData.getEnable());
        if (Objects.isNull(this.strategy)) {
            //提示异常
            return;
        }
        List<ApiProjectDTO> projectConfigList = this.configData.getProjectConfigList(this.moduleName);
        this.projectNameComboBox = new ComboBox<>(projectConfigList.toArray(new ApiProjectDTO[0]));
        if (Objects.isNull(this.apiProjectDTO)) {
            this.apiProjectDTO = projectConfigList.get(0);
        }
        this.projectNameComboBox.setSelectedItem(this.apiProjectDTO);
        this.projectPanel.add(new JLabel(PROJECT_LABEL), BorderLayout.WEST);
        this.projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        //创建项目
        LinkLabel<String> projectLinkLabel = new LinkLabel<>(CREATE_PROJECT, null, (aSource, aLinkData) -> createProject(projectConfigList));
        //当前暂不提供创建项目入口-预留后期
        projectLinkLabel.setEnabled(false);
        projectLinkLabel.setBorder(JBUI.Borders.emptyLeft(10));
        this.projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        this.projectPanel.add(projectLinkLabel, BorderLayout.EAST);
        this.projectPanel.setBorder(JBUI.Borders.empty(10));
        this.categoryPanel.setBorder(JBUI.Borders.empty(10, 10, 20, 10));
        //项目下拉框切换项目事件
        this.projectNameComboBox.addItemListener(e -> {
            //切换当前项目
            apiProjectDTO = (ApiProjectDTO) e.getItem();
            //重置选择分类面板
            resetCategory();
        });
        //初始化接口分类面板
        resetCategory();
    }


    /**
     * 获取当前选中的分类
     *
     * @return 当前选中的项目+选中的分类
     */
    public ApiProjectDTO getSelected() {
        if (this.isCategoryTree) {
            //分类为树形结构时-后期预留
            TreePath selectionPath = treeComponent.getCatalogTree().getSelectionPath();
            Object lastPathComponent;
            if (Objects.nonNull(selectionPath) && Objects.nonNull(lastPathComponent = selectionPath.getLastPathComponent())) {
                apiProjectDTO.setSelectCategory(new ApiCategoryDTO("", lastPathComponent.toString()));
                return apiProjectDTO;
            }
        } else {
            //分类为下拉框时 获取选中的下拉框 apiProjectDTO就是当前选中的项目
            apiProjectDTO.setSelectCategory((ApiCategoryDTO) categoryNameComboBox.getSelectedItem());
            return apiProjectDTO;
        }
        return null;
    }


    /**
     * 重置接口分类
     */
    private void resetCategory() {
        //如果当前项目没有接口分类 则从接口文档系统拉取当前项目下的接口分类
        List<ApiCategoryDTO> apiCategoryList = initCategoryList();
        //清空选择分类面板内容
        this.categoryPanel.removeAll();
        //构建选择分类面板
        this.categoryPanel.add(new JLabel(CATEGORY_LABEL), BorderLayout.WEST);
        if (isCategoryTree) {
            //设置分类树
            this.treeComponent = new FuTreeComponent<>(project, buildCategoryTree(null, this.apiProjectDTO.getProjectName(), null, apiCategoryList));
            this.categoryPanel.add(this.treeComponent.getCatalogTree(), BorderLayout.CENTER);
        } else {
            //设置分类下拉框
            this.categoryNameComboBox = new ComboBox<>(apiCategoryList.toArray(new ApiCategoryDTO[0]));
            this.categoryPanel.add(this.categoryNameComboBox, BorderLayout.CENTER);
            //创建分类链接-点击链接可创建分类
            LinkLabel<String> linkLabel = new LinkLabel<>(CREATE_CATEGORY, null, (aSource, aLinkData) -> createCategory());
            linkLabel.setBorder(JBUI.Borders.emptyLeft(10));
            this.categoryPanel.add(linkLabel, BorderLayout.EAST);
        }
    }


    /**
     * 创建接口分类
     */
    private void createCategory() {
        //弹框让用户输入分类名称
        String value = Messages.showInputDialog(CREATE_CATEGORY_TITLE, CATEGORY_LABEL, Messages.getQuestionIcon(), StringUtils.EMPTY, new CreateCategoryValidator(this.apiProjectDTO));
        if (StringUtils.isNotBlank(value)) {
            //初始化当前项目下的接口分类
            List<ApiCategoryDTO> apiCategoryList = initCategoryList();
            //调用创建分类接口
            ApiCategoryDTO apiCategoryDTO = this.strategy.createCategory(this.configData, this.apiProjectDTO, value);
            if (Objects.isNull(apiCategoryDTO) || StringUtils.isBlank(apiCategoryDTO.getCategoryName()) || StringUtils.isBlank(apiCategoryDTO.getCategoryId())) {
                //创建分类接口失败
                return;
            }
            //将改分类设置选中状态
            apiCategoryList.add(apiCategoryDTO);
            apiProjectDTO.setSelectCategory(apiCategoryDTO);
            categoryNameComboBox.addItem(apiCategoryDTO);
            categoryNameComboBox.setSelectedItem(apiCategoryDTO);
        }
    }


    /**
     * 初始化当前项目下的分类集合
     */
    private List<ApiCategoryDTO> initCategoryList() {
        List<ApiCategoryDTO> apiCategoryList = apiProjectDTO.getApiCategoryList();
        if (CollectionUtils.isEmpty(apiCategoryList)) {
            //如果当前项目下没有分类 则调用第三方接口文档系统查询当前项目下的分类
            apiCategoryList = listCategory();
            apiProjectDTO.setApiCategoryList(apiCategoryList);
        }
        return apiCategoryList;
    }


    /**
     * 查询当前选中项目下的接口分类集合
     */
    private List<ApiCategoryDTO> listCategory() {
        List<ApiCategoryDTO> categoryList = this.strategy.categoryList(this.apiProjectDTO, this.configData);
        return Objects.isNull(categoryList) ? Lists.newArrayList() : categoryList;
    }


    /**
     * 创建项目-预留后期开发
     *
     * @param projectConfigList 当前项目集合
     */
    private void createProject(List<ApiProjectDTO> projectConfigList) {
        List<String> projectNameList = ObjectUtils.listToList(projectConfigList, ApiProjectDTO::getProjectName);
        String value = Messages.showInputDialog(CREATE_PROJECT_TITLE, PROJECT_LABEL, Messages.getQuestionIcon(), StringUtils.EMPTY, new InputExistsValidator(projectNameList));
        //请求创建项目
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectName(value);
        this.projectNameComboBox.addItem(apiProjectDTO);
    }


    /**
     * 构建分类树
     *
     * @param categoryId      分类id
     * @param categoryName    分类名称
     * @param parent          父级分类
     * @param categoryDTOList 分类集合
     * @return 分类节点
     */
    private ApiCategoryTreeNode buildCategoryTree(String categoryId, String categoryName, ApiCategoryTreeNode parent, List<ApiCategoryDTO> categoryDTOList) {
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


    /**
     * 创建分类输入框校验器
     */
    private static class CreateCategoryValidator implements InputValidatorEx {
        //当前选中的项目
        private final ApiProjectDTO apiProjectDTO;
        //错误消息
        private String myErrorText;

        CreateCategoryValidator(ApiProjectDTO apiProjectDTO) {
            this.apiProjectDTO = apiProjectDTO;
        }

        @Override
        public @Nullable String getErrorText(String inputString) {
            return myErrorText;
        }

        @Override
        public boolean checkInput(String inputString) {
            if (StringUtils.isNotBlank(inputString)) {
                //校验输入的内容
                List<ApiCategoryDTO> categoryList = apiProjectDTO.getApiCategoryList();
                if (CollectionUtils.isNotEmpty(categoryList) && categoryList.stream().anyMatch(a -> a.getCategoryName().equals(inputString))) {
                    //当前项目中存在 则不创建
                    myErrorText = FuDocMessageBundle.message(MessageConstants.SYNC_API_CREATE_CATEGORY_REPEAT, apiProjectDTO.getProjectName());
                    return true;
                }
            }
            return false;
        }
    }

}
