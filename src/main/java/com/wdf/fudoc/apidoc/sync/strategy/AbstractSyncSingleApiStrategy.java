package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.date.DateUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ProjectSyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.api.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 同步接口文档至第三方接口文档系统抽象类（每次请求第三方文档系统只同步一个api 多个api需要循环调用接口）
 *
 * @author wangdingfu
 * @date 2022-12-31 22:07:46
 */
public abstract class AbstractSyncSingleApiStrategy extends AbstractSyncApiStrategy implements SyncCategory {


    /**
     * 组装同步到第三方接口文档系统的数据
     */
    protected abstract String doSingleApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO);


    /**
     * 循环同步api到第三方文档系统（针对只能一次同步一个接口的文档系统 例如YApi ShowDoc等）
     *
     * @param fuDocItemDataList 接口文档api集合
     * @param configData        配置数据
     * @param apiProjectDTO     同步项目
     * @param projectRecord     同步记录
     * @return 同步结果
     */
    @Override
    protected List<SyncApiResultDTO> doSyncApi(List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData projectRecord) {
        return fuDocItemDataList.stream().map(f -> Objects.isNull(apiProjectDTO)
                        ? singleSyncApi(configData, f, projectRecord)
                        : singleSyncApi(configData, f, apiProjectDTO, apiProjectDTO.getSelectCategory(), projectRecord))
                .collect(Collectors.toList());
    }


    /**
     * 确认接口需要同步到哪一个分类下
     *
     * @param apiProjectDTO 同步的项目
     * @param configData    配置数据
     * @param psiClass      当前操作的java类
     * @return 指定同步到哪一个分类下的数据对象
     */
    @Override
    protected ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
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
        SyncApiCategoryDialog dialog = new SyncApiCategoryDialog(ProjectUtils.getCurrProject(), false, ModuleUtil.findModuleForPsiElement(psiClass), apiProjectDTO);
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
        String errorMsg = FuStringUtils.EMPTY, apiId = FuStringUtils.EMPTY;
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
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiClass);
        String commentTitle = apiDocCommentData.getCommentTitle();
        return FuStringUtils.isNotBlank(commentTitle) ? commentTitle : psiClass.getName();
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
        resultDTO.setSyncStatus(FuStringUtils.isBlank(errorMsg) ? ApiSyncStatus.SUCCESS.getMessage() : ApiSyncStatus.FAIL.getMessage());
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


}
