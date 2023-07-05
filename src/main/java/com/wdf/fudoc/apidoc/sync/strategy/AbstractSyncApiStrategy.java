package com.wdf.fudoc.apidoc.sync.strategy;

import com.google.common.collect.Lists;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.configurable.FuDocSyncSettingConfigurable;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ProjectSyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableDisableListener;
import com.wdf.fudoc.util.FuDocViewUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.ProjectUtils;
import com.wdf.fudoc.util.ShowSettingUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-06-12 23:00:39
 */
public abstract class AbstractSyncApiStrategy implements SyncFuDocStrategy {
    private static final String NOT_SYNC_API = FuBundle.message(MessageConstants.NOT_SYNC_API);

    /**
     * 检查配置
     *
     * @param configData 第三方接口文档系统相关配置
     */
    protected abstract boolean checkConfig(BaseSyncConfigData configData);

    /**
     * 同步api
     *
     * @param fuDocItemDataList 接口文档api集合
     * @param configData        配置数据
     * @param apiProjectDTO     同步项目
     * @param projectRecord     同步记录
     * @return 同步结果集合
     */
    protected abstract List<SyncApiResultDTO> doSyncApi(List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData projectRecord);

    /**
     * 确认api需要同步至哪个分类下
     *
     * @param apiProjectDTO 同步的项目
     * @param configData    配置数据
     * @param psiClass      当前api所在的java类
     * @param projectRecord 同步记录
     * @return api同步的分类
     */
    protected abstract ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord);


    /**
     * 同步接口文档至第三方文档系统
     *
     * @param fuDocContext 上下文对象
     * @param psiClass     同步的接口所属的class对象
     * @param configData   配置数据
     */
    @Override
    public void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData) {
        //2、检查三方接口文档系统是否能建立连接

        //3、确定当前要同步的项目配置
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleName = Objects.isNull(module) ? org.apache.commons.lang3.StringUtils.EMPTY : module.getName();
        List<ApiProjectDTO> projectConfigList = configData.getProjectConfigList(moduleName);
        if (StringUtils.isBlank(configData.getBaseUrl()) || CollectionUtils.isEmpty(projectConfigList) || checkConfig(configData)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                Project project = psiClass.getProject();
                ShowSettingUtils.showConfigurable(project, new FuDocSyncSettingConfigurable(), 800, 600);
            });
            return;
        }
        ApiProjectDTO apiProjectDTO = projectConfigList.get(0);

        //4、生成接口文档(可能会同时同步多个接口文档)
        List<FuDocItemData> fuDocItemDataList = GenFuDocUtils.gen(fuDocContext, psiClass);
        if (CollectionUtils.isEmpty(fuDocItemDataList)) {
            //发出通知 没有可以同步的接口
            FuDocNotification.notifyWarn(NOT_SYNC_API);
            return;
        }

        //5、同步接口文档
        List<SyncApiResultDTO> resultDTOList = fuDocContext.isSyncDialog()
                //同步api接口-会根据配置自动生成分类名称或者弹框让用户选择分类（无交互式的同步）
                ? autoSyncApi(apiProjectDTO, fuDocItemDataList, configData, psiClass)
                //弹出弹框显示同步进度（有交互式的同步）
                : confirmSyncAPi(apiProjectDTO, fuDocItemDataList, configData, psiClass);

        //6、提示同步结果
        tipSyncResult(configData, resultDTOList);
    }


    /**
     * 智能同步
     * 1、根据配置选择是否自动生成分类名称（无需用户选择分类）. 自动将接口同步至接口文档系统中
     * 2、寻找上一次同步记录 如果有则按上一次同步记录同步
     * 3、弹框让用户选择分类 其中分类按照用户最近使用率排序展示（可手动新增分类）
     *
     * @param apiProjectDTO     当前同步的项目
     * @param fuDocItemDataList 接口文档集合
     * @param configData        配置数据
     * @param psiClass          当前操作的java类
     * @return 同步结果
     */
    private List<SyncApiResultDTO> autoSyncApi(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass) {
        ProjectSyncApiRecordData projectRecord = configData.getProjectRecord(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName());
        if (!configData.isAutoGenCategory() && Objects.nonNull(projectRecord) && fuDocItemDataList.stream().allMatch(a -> projectRecord.exists(a.getUrlList().get(0)))) {
            //按照之前记录同步
            return doSyncApi(fuDocItemDataList, configData, null, initProjectRecord(configData, apiProjectDTO, projectRecord));
        }
        return confirmSyncAPi(apiProjectDTO, fuDocItemDataList, configData, psiClass);
    }


    /**
     * 同步api到第三方接口文档系统-基于弹框让用户选择同步到哪个分类规则
     *
     * @param fuDocItemDataList 同步的接口集合
     * @param configData        第三方接口文档配置
     */
    private List<SyncApiResultDTO> confirmSyncAPi(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass) {
        //初始化同步记录
        ProjectSyncApiRecordData projectRecord = initProjectRecord(configData, apiProjectDTO, configData.getProjectRecord(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName()));
        //弹框选中分类
        ApiProjectDTO confirm = confirmApiCategory(apiProjectDTO, configData, psiClass, projectRecord);
        if (Objects.isNull(confirm)) {
            //没有确认分类 无需发起同步
            return Lists.newArrayList();
        }
        //同步api
        return doSyncApi(fuDocItemDataList, configData, confirm, projectRecord);
    }


    /**
     * 提示同步结果
     *
     * @param resultDTOList 同步结果
     */
    private void tipSyncResult(BaseSyncConfigData configData, List<SyncApiResultDTO> resultDTOList) {
        //需要同步的接口数量(生成的接口文档数量可能和实际需要同步的接口数量不一致 有可能会在弹框中选择哪些接口同步哪些不同步)
        int syncApiSize = resultDTOList.size();
        if (syncApiSize < 1) {
            //本次没有需要同步的接口 不需要提示
            return;
        }
        String apiSystem = configData.getApiSystem().getCode();
        List<SyncApiResultDTO> successList = resultDTOList.stream().filter(a -> ApiSyncStatus.SUCCESS.getMessage().equals(a.getSyncStatus())).collect(Collectors.toList());
        List<SyncApiResultDTO> faileList = resultDTOList.stream().filter(a -> ApiSyncStatus.FAIL.getMessage().equals(a.getSyncStatus())).collect(Collectors.toList());
        FuTableComponent<SyncApiResultDTO> tableComponent = FuTableComponent.create(FuTableColumnFactory.syncApiResult(CollectionUtils.isNotEmpty(faileList)), resultDTOList, SyncApiResultDTO.class);
        tableComponent.addListener(new FuTableDisableListener<>());
        String title = "同步接口至" + apiSystem + "记录列表";
        JPanel showPanel = FuDocViewUtils.createPanel(title, tableComponent.createMainPanel());
        AtomicBoolean pinStatus = FuDocViewUtils.getPinStatus(title);
        SyncApiResultDTO resultDTO = resultDTOList.get(0);
        if (successList.size() == syncApiSize) {
            String apiDocUrl = configData.getApiDocUrl(resultDTO);
            //全部同步成功情况
            if (syncApiSize == 1) {
                //成功同步{0}接口到{0}分类下
                String message = FuBundle.message(MessageConstants.SYNC_API_SUCCESS_ONE, resultDTO.getApiName(), resultDTO.getCategoryName());
                FuDocNotification.notifySyncApiResult(NotificationType.INFORMATION, message, apiSystem, apiDocUrl, showPanel, pinStatus);
                return;
            }
            //本次共计成功同步{0}个接口到{0}分类下
            String message = FuBundle.message(MessageConstants.SYNC_API_SUCCESS_ALL, syncApiSize, resultDTO.getCategoryName());
            FuDocNotification.notifySyncApiResult(NotificationType.INFORMATION, message, apiSystem, apiDocUrl, showPanel, pinStatus);
            return;
        }
        if (faileList.size() == syncApiSize) {
            //全部同步失败情况 - 同步接口失败 失败原因:{0}
            String message = FuBundle.message(MessageConstants.SYNC_API_FAILED_ALL, StringUtils.isNotBlank(resultDTO.getErrorMsg()) ? resultDTO.getErrorMsg() : "未知异常");
            FuDocNotification.notifySyncApiResult(NotificationType.ERROR, message, apiSystem, configData.getApiDocUrl(resultDTO), showPanel, pinStatus);
            return;
        }
        //部分成功 部分失败 - 本次成功同步{0}个接口到{1}分类下 同步失败{2}个接口
        SyncApiResultDTO successResultDTO = successList.get(0);
        String message = FuBundle.message(MessageConstants.SYNC_API_SUCCESS_FAILED, successList.size(), successResultDTO.getCategoryName(), faileList.size());
        FuDocNotification.notifySyncApiResult(NotificationType.WARNING, message, apiSystem, configData.getApiDocUrl(successResultDTO), showPanel, pinStatus);
    }

    private ProjectSyncApiRecordData initProjectRecord(BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData recordData) {
        if (Objects.isNull(recordData)) {
            recordData = new ProjectSyncApiRecordData();
            configData.addProjectRecordData(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName(), recordData);
        }
        return recordData;
    }


    protected SyncApiResultDTO buildSyncApiResult(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiSyncStatus apiSyncStatus, String errorMsg) {
        SyncApiResultDTO syncApiResultDTO = new SyncApiResultDTO();
        syncApiResultDTO.setApiId(fuDocItemData.getApiKey());
        syncApiResultDTO.setApiUrl(fuDocItemData.getUrlList().get(0));
        syncApiResultDTO.setApiName(fuDocItemData.getTitle());
        syncApiResultDTO.setSyncStatus(apiSyncStatus.getMessage());
        syncApiResultDTO.setProjectId(apiProjectDTO.getProjectId());
        syncApiResultDTO.setProjectName(apiProjectDTO.getProjectName());
        syncApiResultDTO.setCategoryName(apiProjectDTO.getSelectCategory().getCategoryName());
        syncApiResultDTO.setErrorMsg(errorMsg);
        return syncApiResultDTO;
    }


    protected String recursionPath(ApiCategoryDTO apiCategoryDTO) {
        if (Objects.isNull(apiCategoryDTO) || Objects.isNull(apiCategoryDTO.getParent())) {
            return StringUtils.EMPTY;
        }
        String parentName = recursionPath(apiCategoryDTO.getParent());
        String categoryName = apiCategoryDTO.getCategoryName();
        return StringUtils.isBlank(parentName) ? categoryName : parentName + "/" + categoryName;
    }
}
