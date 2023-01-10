package com.wdf.fudoc.apidoc.sync.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.view.SyncApiView;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步接口文档至第三方接口文档系统抽象类
 *
 * @author wangdingfu
 * @date 2022-12-31 22:07:46
 */
public abstract class AbstractSyncFuDocStrategy implements SyncFuDocStrategy {

    /**
     * 检查配置
     *
     * @param baseSyncConfigData 第三方接口文档系统相关配置
     */
    protected abstract boolean checkConfig(BaseSyncConfigData baseSyncConfigData);


    /**
     * 组装同步到第三方接口文档系统的数据
     */
    protected abstract String doSync(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO);


    /**
     * 获取当前同步的项目配置
     *
     * @param configData 配置数据
     * @param psiClass   当前操作的class
     * @return 本次同步的项目
     */
    protected abstract ApiProjectDTO getSyncProjectConfig(BaseSyncConfigData configData, PsiClass psiClass);


    @Override
    public void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData) {
        //1、检查三方接口文档配置
        if (!checkConfig(configData)) {
            return;
        }

        //2、检查三方接口文档系统是否能建立连接

        //3、生成接口文档(可能会同时同步多个接口文档)
        List<FuDocItemData> fuDocItemDataList = GenFuDocUtils.gen(fuDocContext, psiClass);
        if (CollectionUtils.isEmpty(fuDocItemDataList)) {
            //发出通知 没有可以同步的接口
            return;
        }
        //4、确定当前要同步的项目配置
        ApiProjectDTO apiProjectDTO = getSyncProjectConfig(configData, psiClass);

        //5、同步接口文档
        List<SyncApiResultDTO> resultDTOList = fuDocContext.isSyncDialog() ? syncApiList(apiProjectDTO, fuDocItemDataList, configData, psiClass) : syncApiForDialog(apiProjectDTO, fuDocItemDataList, configData);

        //6、同步结果提示

    }


    private List<SyncApiResultDTO> syncApiList(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, PsiClass psiClass) {
        if (!configData.isAutoGenCategory() && fuDocItemDataList.stream().allMatch(a -> configData.isRecord(a.getUrlList().get(0)))) {
            //按照之前记录同步
            return fuDocItemDataList.stream().map(f -> doSyncApi(configData, f, apiProjectDTO, configData.getRecord(f.getUrlList().get(0)).getCategory())).collect(Collectors.toList());
        }
        //确认需要同步的分类
        ApiProjectDTO confirm = confirmApiCategory(apiProjectDTO, configData, psiClass);
        if (Objects.isNull(confirm)) {
            FuDocNotification.notifyWarn("本次同步失败, 没有选定需要同步的接口分类");
            return Lists.newArrayList();
        }
        //将本次接口均同步至该分类下
        return fuDocItemDataList.stream().map(f -> doSyncApi(configData, f, confirm, confirm.getSelectCategory())).collect(Collectors.toList());
    }


    private SyncApiResultDTO doSyncApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        SyncApiResultDTO resultDTO = new SyncApiResultDTO();
        String errorMsg = doSync(configData, fuDocItemData, apiProjectDTO, apiCategoryDTO);
        resultDTO.setApiName(fuDocItemData.getTitle());
        resultDTO.setApiUrl(fuDocItemData.getUrlList().get(0));
        resultDTO.setProjectName(apiProjectDTO.getProjectName());
        resultDTO.setCategoryName(apiCategoryDTO.getCategoryName());
        resultDTO.setSyncStatus(StringUtils.isBlank(errorMsg) ? ApiSyncStatus.SUCCESS.getMessage() : ApiSyncStatus.FAIL.getMessage());
        resultDTO.setErrorMsg(errorMsg);
        return resultDTO;
    }

    /**
     * 同步api到第三方接口文档系统-基于弹框让用户选择同步到哪个分类规则
     *
     * @param fuDocItemDataList  同步的接口集合
     * @param baseSyncConfigData 第三方接口文档配置
     */
    private List<SyncApiResultDTO> syncApiForDialog(ApiProjectDTO apiProjectDTO, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData baseSyncConfigData) {
        //弹框让用户选择需要同步的目录


        SyncApiView syncApiView = new SyncApiView(ProjectUtils.getCurrProject());
        if (!syncApiView.showAndGet()) {
            //选择取消 则不同步接口
        }
        List<SyncApiTableData> tableDataList = syncApiView.getTableDataList();
        if (CollectionUtils.isEmpty(tableDataList)) {
            //没有选择接口同步 直接返回
        }
        return Lists.newArrayList();
    }


    private ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass) {
        if (configData.isAutoGenCategory()) {
            //自动生成接口分类 无需用户选择
            String categoryName = getApiCategoryFromController(psiClass);
            ApiCategoryDTO matchCategory = matchCategory(categoryName, apiProjectDTO.getApiCategoryList());
            if (Objects.isNull(matchCategory)) {
                //创建一个接口分类
                matchCategory = createCategory(configData, apiProjectDTO, categoryName);
            }
            apiProjectDTO.setSelectCategory(matchCategory);
            return apiProjectDTO;
        }
        //弹框让用户选择要同步的分类
        SyncApiCategoryDialog dialog = new SyncApiCategoryDialog(ProjectUtils.getCurrProject(), false, apiProjectDTO.getModuleName(), apiProjectDTO);
        dialog.show();
        ApiProjectDTO selectCategory = dialog.getSelectCategory();
        return selectCategory;
    }


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


    private String getApiCategoryFromController(PsiClass psiClass) {
        //获取Controller上的标题
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(psiClass.getDocComment());
        String commentTitle = apiDocCommentData.getCommentTitle();
        return StringUtils.isNotBlank(commentTitle) ? commentTitle : psiClass.getName();
    }


}
