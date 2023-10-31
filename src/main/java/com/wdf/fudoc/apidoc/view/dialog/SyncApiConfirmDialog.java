package com.wdf.fudoc.apidoc.view.dialog;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.JBUI;
import com.wdf.api.notification.FuDocNotification;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.api.base.FuBundle;
import com.wdf.api.constants.MessageConstants;
import com.wdf.fudoc.components.tree.FuTreeActionListener;
import com.wdf.fudoc.components.tree.FuTreeComponent;
import com.wdf.fudoc.components.tree.node.FuTreeNode;
import com.wdf.fudoc.components.validator.CreateCategoryValidator;
import com.wdf.fudoc.util.FuDocUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @date 2023-07-01 23:02:24
 */
public class SyncApiConfirmDialog extends DialogWrapper implements FuTreeActionListener<ApiCategoryDTO> {

    private final JPanel rootPanel;
    private final JPanel categoryPanel;

    private final Project project;

    private ApiProjectDTO apiProjectDTO;

    private List<ApiProjectDTO> projectConfigList;

    /**
     * 项目名称下拉框
     */
    private ComboBox<ApiProjectDTO> projectNameComboBox;

    /**
     * 第三方接口文档系统配置数据
     */
    private BaseSyncConfigData configData;

    /**
     * 当前所属module
     */
    private final Module module;

    /**
     * 当前同步文档的class
     */
    private final PsiClass psiClass;

    /***
     * 默认分类
     */
    private ApiCategoryDTO defaultApiCategory;

    /**
     * 树形组件
     */
    private FuTreeComponent<ApiCategoryDTO> fuTreeComponent;

    private static final String PROJECT_LABEL = FuBundle.message(MessageConstants.SYNC_API_PROJECT_LABEL);
    private static final String CREATE_PROJECT = FuBundle.message(MessageConstants.SYNC_API_CREATE_PROJECT);
    private static final String CREATE_CATEGORY_TITLE = FuBundle.message(MessageConstants.SYNC_API_CREATE_CATEGORY_TITLE);
    private static final String CATEGORY_LABEL = FuBundle.message(MessageConstants.SYNC_API_CATEGORY_LABEL);


    public SyncApiConfirmDialog(@Nullable Project project, PsiClass psiClass) {
        super(project, true);
        this.project = project;
        this.psiClass = psiClass;
        this.rootPanel = new JPanel(new BorderLayout());
        this.categoryPanel = new JPanel(new BorderLayout());
        this.module = ModuleUtil.findModuleForPsiElement(psiClass);
        //初始化配置数据
        initConfig();
        //初始化UI
        initProjectUI();
        init();
        setTitle("选择api所属目录");
    }

    private void initConfig() {
        //填充项目名称下拉框
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        this.configData = settingData.getEnableConfigData();
        this.projectConfigList = this.configData.getProjectConfigList(module);
        this.projectNameComboBox = new ComboBox<>(projectConfigList.toArray(new ApiProjectDTO[0]));
        //监听项目变更时 刷新树形组件
        this.projectNameComboBox.addItemListener(e -> {
            if (ItemEvent.SELECTED == e.getStateChange()) {
                projectChangeEvent((ApiProjectDTO) e.getItem());
            }
        });
        //自动选择一个项目
        ApiProjectDTO autoSelectProject = autoSelectProject();
        if (Objects.nonNull(autoSelectProject)) {
            this.projectNameComboBox.setSelectedItem(autoSelectProject);
        }

    }

    private ApiProjectDTO autoSelectProject() {
        if (CollectionUtils.isEmpty(this.projectConfigList)) {
            FuDocNotification.notifyError("请添加项目配置后在同步文档");
            return null;
        }
        ApiProjectDTO autoSelected = projectConfigList.stream().filter(ApiProjectDTO::isLatest).findFirst().orElse(projectConfigList.get(0));
        //是否将当前类的注释信息作为分类
        String title = FuDocUtils.classTitle(psiClass);
        if (StringUtils.isNotBlank(title)) {
            List<ApiCategoryDTO> apiCategoryList = autoSelected.getApiCategoryList();
            if (Objects.isNull(apiCategoryList)) {
                apiCategoryList = Lists.newArrayList();
                autoSelected.setApiCategoryList(apiCategoryList);
            }
            if (apiCategoryList.stream().noneMatch(a -> title.equals(a.getCategoryName()))) {
                this.defaultApiCategory = new ApiCategoryDTO(title);
                apiCategoryList.add(this.defaultApiCategory);
            }
        }
        return autoSelected;
    }


    private void projectChangeEvent(ApiProjectDTO selected) {
        this.projectConfigList.forEach(f -> f.setLatest(false));
        this.apiProjectDTO = selected;
        this.apiProjectDTO.setLatest(true);
        //初始化树形组件
        this.fuTreeComponent = new FuTreeComponent<>(project, buildRoot(this.apiProjectDTO.getApiCategoryList()), this);
        //切换树形组件面板
        switchPanel(fuTreeComponent.createUI());
        //保存到持久化文件中
        configData.syncApiProjectList(module, this.projectConfigList);
    }


    /**
     * 切换面板
     *
     * @param switchPanel 需要切换的面板
     */
    private void switchPanel(JComponent switchPanel) {
        this.categoryPanel.removeAll();
        this.categoryPanel.repaint();
        this.categoryPanel.add(switchPanel, BorderLayout.CENTER);
        this.categoryPanel.revalidate();
    }


    private void initProjectUI() {
        JPanel projectPanel = new JPanel(new BorderLayout());
        projectPanel.add(new JLabel(PROJECT_LABEL), BorderLayout.WEST);
        projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        //创建项目
        LinkLabel<String> projectLinkLabel = new LinkLabel<>(CREATE_PROJECT, null, (aSource, aLinkData) -> {
            //当前暂不提供创建项目入口-预留后期
        });
        if (Objects.isNull(this.apiProjectDTO)) {
            projectChangeEvent(autoSelectProject());
        }
        projectLinkLabel.setEnabled(false);
        projectLinkLabel.setBorder(JBUI.Borders.emptyLeft(10));
        projectPanel.add(projectLinkLabel, BorderLayout.EAST);
        projectPanel.setBorder(JBUI.Borders.empty(10));
        this.rootPanel.add(projectPanel, BorderLayout.NORTH);
        this.rootPanel.add(this.categoryPanel, BorderLayout.CENTER);
    }


    /**
     * 获取选中需要同步的分类
     */
    public ApiProjectDTO getSelected() {
        FuTreeNode<ApiCategoryDTO> selectedNode = fuTreeComponent.getSelectedNode();
        this.apiProjectDTO.setSelectCategory(Objects.isNull(selectedNode) ? this.defaultApiCategory : selectedNode.getData());
        return this.apiProjectDTO;
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        this.rootPanel.setPreferredSize(new Dimension(400, 400));
        this.rootPanel.setMaximumSize(new Dimension(300, 200));
        return this.rootPanel;
    }

    /**
     * 新增节点
     *
     * @param parent 父节点
     * @return 新增的节点
     */
    @Override
    public FuTreeNode<ApiCategoryDTO> createNode(FuTreeNode<ApiCategoryDTO> parent) {
        TreeNode[] paths = parent.getPath();
        if (Objects.isNull(paths) || paths.length == 0) {
            return null;
        }
        //当前选中节点的子集分类
        ApiCategoryDTO apiCategoryDTO = findApiCategory(paths);
        if (Objects.isNull(apiCategoryDTO)) {
            return null;
        }
        List<ApiCategoryDTO> apiCategoryList = apiCategoryDTO.getApiCategoryList();
        //弹框让用户输入分类名称
        String categoryName = Messages.showInputDialog(CREATE_CATEGORY_TITLE, CATEGORY_LABEL, Messages.getQuestionIcon(), StringUtils.EMPTY, new CreateCategoryValidator(apiCategoryList));
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }
        ApiCategoryDTO addCategory = new ApiCategoryDTO(categoryName);
        addCategory.setParent(apiCategoryDTO);
        apiCategoryList.add(addCategory);
        //保存到持久化文件中
        configData.syncApiProjectList(module, this.projectConfigList);
        return new FuTreeNode<>(addCategory, AllIcons.Nodes.Package);
    }

    @Override
    public boolean removeNode(FuTreeNode<ApiCategoryDTO> parent) {
        TreeNode[] paths = parent.getPath();
        if (Objects.isNull(paths) || paths.length == 0) {
            return false;
        }
        if (paths.length == 1) {
            //根目录无法删除
            return false;
        }
        //当前选中节点的子集分类
        ApiCategoryDTO apiCategoryDTO = findApiCategory(paths);
        ApiCategoryDTO parentCategory = apiCategoryDTO.getParent();
        if (Objects.isNull(parentCategory)) {
            return false;
        }
        List<ApiCategoryDTO> apiCategoryList = parentCategory.getApiCategoryList();
        if (CollectionUtils.isEmpty(apiCategoryList)) {
            return false;
        }
        String removeNode = paths[paths.length - 1].toString();
        apiCategoryList.removeIf(f -> f.getCategoryName().equals(removeNode));
        //保存到持久化文件中
        configData.syncApiProjectList(module, this.projectConfigList);
        return true;
    }


    public ApiCategoryDTO findApiCategory(TreeNode[] paths) {
        List<ApiCategoryDTO> apiCategoryList = apiProjectDTO.getApiCategoryList();
        if (Objects.isNull(apiCategoryList)) {
            apiCategoryList = Lists.newArrayList();
            apiProjectDTO.setApiCategoryList(apiCategoryList);
        }
        //构建根节点
        ApiCategoryDTO root = new ApiCategoryDTO("根目录", apiCategoryList);
        apiCategoryList.forEach(f -> f.setParent(root));
        //递归查找分类
        return recursionCategory(paths, 1, root);
    }


    /**
     * 递归遍历分类树
     *
     * @param paths  分类在树结构上的路径
     * @param index  遍历的节点层级
     * @param parent 父分类
     * @return 选中节点的分类
     */
    private ApiCategoryDTO recursionCategory(TreeNode[] paths, int index, ApiCategoryDTO parent) {
        List<ApiCategoryDTO> apiCategoryList = parent.getApiCategoryList();
        if (Objects.isNull(apiCategoryList)) {
            apiCategoryList = Lists.newArrayList();
            parent.setApiCategoryList(apiCategoryList);
        }
        if (index >= paths.length) {
            return parent;
        }
        String nodeName = paths[index].toString();
        Optional<ApiCategoryDTO> first = apiCategoryList.stream().filter(f -> nodeName.equals(f.getCategoryName())).findFirst();
        if (first.isEmpty()) {
            ApiCategoryDTO apiCategoryDTO = new ApiCategoryDTO(nodeName, parent);
            apiCategoryList.add(apiCategoryDTO);
            return recursionCategory(paths, ++index, apiCategoryDTO);
        }
        return recursionCategory(paths, ++index, first.get());
    }


    /**
     * 构建分类根节点
     *
     * @param apiCategoryDTOList 分类集合
     * @return 分类根节点
     */
    private FuTreeNode<ApiCategoryDTO> buildRoot(List<ApiCategoryDTO> apiCategoryDTOList) {
        FuTreeNode<ApiCategoryDTO> root = new FuTreeNode<>(new ApiCategoryDTO("根目录"), AllIcons.Nodes.Folder);
        if (CollectionUtils.isNotEmpty(apiCategoryDTOList)) {
            apiCategoryDTOList.forEach(f -> buildNode(f, root));
        }
        return root;
    }

    /**
     * 递归构建树的节点
     *
     * @param apiCategoryDTO 分类对象
     * @param node           父节点
     */
    private void buildNode(ApiCategoryDTO apiCategoryDTO, FuTreeNode<ApiCategoryDTO> node) {
        if (Objects.isNull(apiCategoryDTO)) {
            return;
        }
        FuTreeNode<ApiCategoryDTO> childNode = new FuTreeNode<>(apiCategoryDTO, AllIcons.Nodes.Package);
        List<ApiCategoryDTO> apiCategoryList = apiCategoryDTO.getApiCategoryList();
        if (CollectionUtils.isNotEmpty(apiCategoryList)) {
            apiCategoryList.forEach(f -> buildNode(f, childNode));
        }
        apiCategoryDTO.setParent(node.getData());
        node.add(childNode);
    }


}
