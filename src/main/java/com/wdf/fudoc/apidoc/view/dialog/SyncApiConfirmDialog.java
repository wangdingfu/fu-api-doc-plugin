package com.wdf.fudoc.apidoc.view.dialog;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.components.tree.FuModuleTreeStructure;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-01 23:02:24
 */
public class SyncApiConfirmDialog extends DialogWrapper {

    private final JPanel rootPanel;
    private final Project project;

    private ApiProjectDTO apiProjectDTO;

    /**
     * 项目名称下拉框
     */
    private ComboBox<ApiProjectDTO> projectNameComboBox;

    /**
     * 第三方接口文档系统配置数据
     */
    private BaseSyncConfigData configData;

    private final Module module;

    /**
     * 分类目录树
     */
    private SimpleTree simpleTree;


    private static final String PROJECT_LABEL = FuDocMessageBundle.message(MessageConstants.SYNC_API_PROJECT_LABEL);


    public SyncApiConfirmDialog(@Nullable Project project, PsiClass psiClass) {
        super(project, true);
        this.project = project;
        this.rootPanel = new JPanel(new BorderLayout());
        this.module = ModuleUtil.findModuleForPsiElement(psiClass);
        //初始化配置数据
        initConfig();
        //初始化UI
        initProjectUI();
        initCategoryUI();
        init();
        setTitle("选择同步到指定分类下");
    }

    private void initConfig() {
        //填充项目名称下拉框
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        this.configData = settingData.getEnableConfigData();
        List<ApiProjectDTO> projectConfigList = this.configData.getProjectConfigList(Objects.isNull(module) ? StringUtils.EMPTY : module.getName());
        this.projectNameComboBox = new ComboBox<>(projectConfigList.toArray(new ApiProjectDTO[0]));
        if (Objects.isNull(this.apiProjectDTO)) {
            this.apiProjectDTO = projectConfigList.get(0);
        }
    }

    private void initProjectUI() {
        JPanel projectPanel = new JPanel();
        projectPanel.add(new JLabel(PROJECT_LABEL), BorderLayout.WEST);
        projectPanel.add(this.projectNameComboBox, BorderLayout.CENTER);
        ActionLink actionLink = new ActionLink("创建一个项目", e -> {
            //创建一个项目 目前暂不开放
        });
        actionLink.setEnabled(false);
        projectPanel.add(actionLink, BorderLayout.EAST);
        projectPanel.setBorder(JBUI.Borders.empty(10));
        this.rootPanel.add(projectPanel, BorderLayout.NORTH);
    }

    private void initCategoryUI() {
        FuModuleTreeStructure fuModuleTreeStructure = new FuModuleTreeStructure(project);
        StructureTreeModel<FuModuleTreeStructure> treeModel = new StructureTreeModel<>(fuModuleTreeStructure, null, getDisposable());
        AsyncTreeModel asyncTreeModel = new AsyncTreeModel(treeModel, getDisposable());
        this.simpleTree = new SimpleTree(asyncTreeModel);
        this.rootPanel.add(ToolbarDecorator.createDecorator(this.simpleTree).createPanel(), BorderLayout.CENTER);
    }


    /**
     * 获取选中需要同步的分类
     */
    public ApiProjectDTO getSelected() {
        TreePath selectionPath = this.simpleTree.getSelectionPath();
        if (Objects.isNull(selectionPath)) {
            return null;
        }
        Object[] path = selectionPath.getPath();

        ApiCategoryDTO apiCategoryDTO = new ApiCategoryDTO();
        apiCategoryDTO.setCategoryName("测试");
        apiProjectDTO.setSelectCategory(apiCategoryDTO);
        return apiProjectDTO;
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        this.rootPanel.setPreferredSize(new Dimension(600, 600));
        this.rootPanel.setMaximumSize(new Dimension(600, 600));
        return this.rootPanel;
    }
}
