package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.configurable.FuDocSyncSettingConfigurable;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.util.FuDocViewUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步接口文档至第三方接口文档系统抽象类
 *
 * @author wangdingfu
 * @date 2022-12-31 22:07:46
 */
public abstract class AbstractSyncFuDocStrategy implements SyncFuDocStrategy {

    private static final String NOT_SYNC_API = FuDocMessageBundle.message(MessageConstants.NOT_SYNC_API);

    /**
     * 检查配置
     *
     * @param configData 第三方接口文档系统相关配置
     */
    protected abstract boolean checkConfig(BaseSyncConfigData configData);


    /**
     * 组装同步到第三方接口文档系统的数据
     */
    protected abstract String doSingleApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO);


    @Override
    public void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData) {
        //1、检查三方接口文档配置
        if (!checkConfig(configData)) {
            return;
        }
        //2、检查三方接口文档系统是否能建立连接

        //3、确定当前要同步的项目配置
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleName = Objects.isNull(module) ? org.apache.commons.lang3.StringUtils.EMPTY : module.getName();
        List<ApiProjectDTO> projectConfigList = configData.getProjectConfigList(moduleName);
        if (StringUtils.isBlank(configData.getBaseUrl()) || CollectionUtils.isEmpty(projectConfigList)) {
            DumbService.getInstance(psiClass.getProject()).smartInvokeLater(() -> {
                //弹框让用户去创建项目
                ShowSettingsUtil.getInstance().showSettingsDialog(psiClass.getProject(), FuDocSyncSettingConfigurable.class);
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
                ? intelligenceSyncApi(apiProjectDTO, fuDocItemDataList, configData, psiClass)
                //弹出弹框显示同步进度（有交互式的同步）
                : syncApiForDialog(apiProjectDTO, fuDocItemDataList, configData, psiClass);

        //6、提示同步结果
        tipSyncResult(configData, resultDTOList);
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
        List<SyncApiResultDTO> successList = resultDTOList.stream().filter(a -> ApiSyncStatus.SUCCESS.getMessage().equals(a.getSyncStatus())).toList();
        List<SyncApiResultDTO> faileList = resultDTOList.stream().filter(a -> ApiSyncStatus.FAIL.getMessage().equals(a.getSyncStatus())).toList();
        FuTableComponent<SyncApiResultDTO> tableComponent = FuTableComponent.create(FuTableColumnFactory.syncApiResult(CollectionUtils.isNotEmpty(faileList)), resultDTOList, SyncApiResultDTO.class);
        tableComponent.addListener(new FuTableListener<>() {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //设置不可编辑
                return false;
            }
        });

        JPanel showPanel = FuDocViewUtils.createPanel("同步接口至" + apiSystem + "记录列表", tableComponent.createMainPanel());
        SyncApiResultDTO resultDTO = resultDTOList.get(0);
        if (successList.size() == syncApiSize) {
            String apiDocUrl = configData.getApiDocUrl(resultDTO);
            //全部同步成功情况
            if (syncApiSize == 1) {
                //成功同步{0}接口到{0}分类下
                String message = FuDocMessageBundle.message(MessageConstants.SYNC_API_SUCCESS_ONE, resultDTO.getApiName(), resultDTO.getCategoryName());
                FuDocNotification.notifySyncApiResult(NotificationType.INFORMATION, message, apiSystem, apiDocUrl, showPanel);
                return;
            }
            //本次共计成功同步{0}个接口到{0}分类下
            String message = FuDocMessageBundle.message(MessageConstants.SYNC_API_SUCCESS_ALL, syncApiSize, resultDTO.getCategoryName());
            FuDocNotification.notifySyncApiResult(NotificationType.INFORMATION, message, apiSystem, apiDocUrl, showPanel);
            return;
        }
        if (faileList.size() == syncApiSize) {
            //全部同步失败情况 - 同步接口失败 失败原因:{0}
            String message = FuDocMessageBundle.message(MessageConstants.SYNC_API_FAILED_ALL, StringUtils.isNotBlank(resultDTO.getErrorMsg()) ? resultDTO.getErrorMsg() : "未知异常");
            FuDocNotification.notifySyncApiResult(NotificationType.ERROR, message, apiSystem, configData.getApiDocUrl(resultDTO), showPanel);
            return;
        }
        //部分成功 部分失败 - 本次成功同步{0}个接口到{1}分类下 同步失败{2}个接口
        SyncApiResultDTO successResultDTO = successList.get(0);
        String message = FuDocMessageBundle.message(MessageConstants.SYNC_API_SUCCESS_FAILED, successList.size(), successResultDTO.getCategoryName(), faileList.size());
        FuDocNotification.notifySyncApiResult(NotificationType.WARNING, message, apiSystem, configData.getApiDocUrl(successResultDTO), showPanel);
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
    private List<SyncApiResultDTO> intelligenceSyncApi(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass) {
        ProjectSyncApiRecordData projectRecord = configData.getProjectRecord(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName());
        if (!configData.isAutoGenCategory() && Objects.nonNull(projectRecord) && fuDocItemDataList.stream().allMatch(a -> projectRecord.exists(a.getUrlList().get(0)))) {
            //按照之前记录同步
            return fuDocItemDataList.stream().map(f -> singleSyncApi(configData, f, initProjectRecord(configData, apiProjectDTO, projectRecord))).collect(Collectors.toList());
        }
        return confirmCategorySync(apiProjectDTO, fuDocItemDataList, configData, psiClass, initProjectRecord(configData, apiProjectDTO, projectRecord));
    }


    /**
     * 发起单条接口同步-获取历史同步记录数据发起同步
     *
     * @param configData    接口文档系统配置
     * @param fuDocItemData 接口文档
     * @param projectRecord 当前项目同步记录
     * @return 同步结果
     */
    private SyncApiResultDTO singleSyncApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ProjectSyncApiRecordData projectRecord) {
        SyncApiRecordData record = projectRecord.getRecord(fuDocItemData.getUrlList().get(0));
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectId(record.getProjectId());
        apiProjectDTO.setProjectName(record.getProjectName());
        apiProjectDTO.setProjectToken(record.getProjectToken());
        return singleSyncApi(configData, fuDocItemData, apiProjectDTO, record.getCategory(), projectRecord);
    }


    /**
     * 发起单条接口同步-同步成功需要保存同步记录
     *
     * @param configData     接口文档系统配置
     * @param fuDocItemData  接口文档
     * @param apiProjectDTO  同步的项目
     * @param apiCategoryDTO 同步的分类
     * @return 同步结果
     */
    private SyncApiResultDTO singleSyncApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO, ProjectSyncApiRecordData projectRecord) {
        String errorMsg = StringUtils.EMPTY, apiId = StringUtils.EMPTY;
        try {
            apiId = doSingleApi(configData, fuDocItemData, apiProjectDTO, apiCategoryDTO);
            //如果接口同步成功 则记录下来
            projectRecord.addRecord(buildApiSyncRecord(fuDocItemData, apiProjectDTO, apiCategoryDTO));
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        //构建返回结果
        return buildResult(apiId, fuDocItemData, apiProjectDTO, apiCategoryDTO, errorMsg);
    }


    /**
     * 同步api到第三方接口文档系统-基于弹框让用户选择同步到哪个分类规则
     *
     * @param fuDocItemDataList 同步的接口集合
     * @param configData        第三方接口文档配置
     */
    private List<SyncApiResultDTO> syncApiForDialog(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass) {
        //目前暂定弹出该窗口
        return confirmCategorySync(apiProjectDTO, fuDocItemDataList, configData, psiClass, initProjectRecord(configData, apiProjectDTO, configData.getProjectRecord(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName())));
    }


    /**
     * 弹框确认分类同步
     *
     * @return 同步结果
     */
    private List<SyncApiResultDTO> confirmCategorySync(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
        //确认需要同步的分类
        StringBuilder errorMsg = new StringBuilder();
        try {
            ApiProjectDTO confirm = confirmApiCategory(apiProjectDTO, configData, psiClass, projectRecord);
            if (Objects.isNull(confirm)) {
                //没有确认分类 无需发起同步
                return Lists.newArrayList();
            }
            //将本次接口均同步至本次确认的分类下
            return fuDocItemDataList.stream().map(f -> singleSyncApi(configData, f, confirm, confirm.getSelectCategory(), projectRecord)).collect(Collectors.toList());
        } catch (Exception e) {
            //确认分类失败 记录异常原因
            errorMsg.append(e.getMessage());
        }
        //构建同步失败结果
        return ObjectUtils.listToList(fuDocItemDataList, fudoc -> buildResult("", fudoc, apiProjectDTO, apiProjectDTO.getSelectCategory(), errorMsg.toString()));
    }


    /**
     * 确认接口需要同步到哪一个分类下
     *
     * @param apiProjectDTO 同步的项目
     * @param configData    配置数据
     * @param psiClass      当前操作的java类
     * @return 指定同步到哪一个分类下的数据对象
     */
    private ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
        if (configData.isAutoGenCategory()) {
            //自动生成接口分类 无需用户选择 自动生成分类名称
            String categoryName = getApiCategoryFromController(psiClass);
            //匹配是否存在该分类 不存在则创建该分类
            ApiCategoryDTO matchCategory = matchCategory(categoryName, apiProjectDTO.getApiCategoryList());
            if (Objects.isNull(matchCategory)) {
                matchCategory = createCategory(configData, apiProjectDTO, categoryName);
            }
            apiProjectDTO.setSelectCategory(matchCategory);
            return apiProjectDTO;
        }
        //弹框让用户选择要同步的分类
        SyncApiCategoryDialog dialog = new SyncApiCategoryDialog(ProjectUtils.getCurrProject(), false, apiProjectDTO.getModuleName(), apiProjectDTO);
        if (dialog.showAndGet()) {
            //弹框点OK时获取选中的分类数据
            ApiProjectDTO selected = dialog.getSelected();
            //添加当前选中的分类到历史记录中 根据该记录会对分类进行排序
            projectRecord.addCategory(selected.getProjectName(), selected.getSelectCategory().getCategoryName());
            return selected;
        }
        return null;
    }


    /**
     * 从分类集合中匹配指定分类是否存在
     * TODO 后期考虑路径问题
     *
     * @param categoryName    分类名称
     * @param categoryDTOList 分类集合
     * @return 匹配的分类
     */
    private ApiCategoryDTO matchCategory(String categoryName, List<ApiCategoryDTO> categoryDTOList) {
        if (CollectionUtils.isNotEmpty(categoryDTOList)) {
            for (ApiCategoryDTO apiCategoryDTO : categoryDTOList) {
                if (categoryName.equals(apiCategoryDTO.getCategoryName())) {
                    return apiCategoryDTO;
                }
                ApiCategoryDTO matchCategory = matchCategory(categoryName, apiCategoryDTO.getApiCategoryList());
                if (Objects.nonNull(matchCategory)) {
                    return matchCategory;
                }
            }
        }
        return null;
    }


    /**
     * 获取当前操作类上的备注
     *
     * @param psiClass 当前操作的java类
     * @return java类上的备注
     */
    private String getApiCategoryFromController(PsiClass psiClass) {
        //获取Controller上的标题
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiClass.getDocComment());
        String commentTitle = apiDocCommentData.getCommentTitle();
        return StringUtils.isNotBlank(commentTitle) ? commentTitle : psiClass.getName();
    }


    /**
     * 构建同步结果
     *
     * @param fuDocItemData  接口文档
     * @param apiProjectDTO  同步的项目
     * @param apiCategoryDTO 同步的分类
     * @param errorMsg       同步失败原因
     * @return 同步结果对象
     */
    private SyncApiResultDTO buildResult(String apiId, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO, String errorMsg) {
        SyncApiResultDTO resultDTO = new SyncApiResultDTO();
        resultDTO.setApiId(apiId);
        resultDTO.setApiName(fuDocItemData.getTitle());
        resultDTO.setApiUrl(fuDocItemData.getUrlList().get(0));
        resultDTO.setProjectId(apiProjectDTO.getProjectId());
        resultDTO.setProjectName(apiProjectDTO.getProjectName());
        if (Objects.nonNull(apiCategoryDTO)) {
            resultDTO.setCategoryId(apiCategoryDTO.getCategoryId());
            resultDTO.setCategoryName(apiCategoryDTO.getCategoryName());
        }
        resultDTO.setSyncStatus(StringUtils.isBlank(errorMsg) ? ApiSyncStatus.SUCCESS.getMessage() : ApiSyncStatus.FAIL.getMessage());
        resultDTO.setErrorMsg(errorMsg);
        return resultDTO;
    }


    /**
     * 构建同步记录
     *
     * @param fuDocItemData 接口文档
     * @param apiProjectDTO 同步的项目
     * @return 同步记录
     */
    private SyncApiRecordData buildApiSyncRecord(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        SyncApiRecordData record = new SyncApiRecordData();
        record.setProjectId(apiProjectDTO.getProjectId());
        record.setProjectName(apiProjectDTO.getProjectName());
        record.setProjectToken(apiProjectDTO.getProjectToken());
        record.setCategory(apiCategoryDTO);
        record.setSyncTime(DateUtil.now());
        record.setApiUrl(fuDocItemData.getUrlList().get(0));
        return record;
    }

    private ProjectSyncApiRecordData initProjectRecord(BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData recordData) {
        if (Objects.isNull(recordData)) {
            recordData = new ProjectSyncApiRecordData();
            configData.addProjectRecordData(ProjectUtils.getCurrentProjectPath(), apiProjectDTO.getProjectName(), recordData);
        }
        return recordData;
    }

}
