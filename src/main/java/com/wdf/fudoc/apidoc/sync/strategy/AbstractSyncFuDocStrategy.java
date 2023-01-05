package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.sync.view.SyncApiView;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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
    protected abstract void checkConfig(BaseSyncConfigData baseSyncConfigData);


    /**
     * 组装同步到第三方接口文档系统的数据
     *
     * @param syncApiData 同步的接口数据
     * @return 第三方接口文档系统需要的数据
     */
    protected abstract String assembleSyncData(SyncApiData syncApiData);


    /**
     * 校验同步后的返回结果
     *
     * @param result 同步接口文档请求的结果
     */
    protected abstract String checkSyncResult(String result);


    @Override
    public void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData) {
        //1、检查三方接口文档配置
        checkConfig(configData);

        //2、检查三方接口文档系统是否能建立连接

        //3、生成接口文档(可能会同时同步多个接口文档)
        List<FuDocItemData> fuDocItemDataList = GenFuDocUtils.gen(fuDocContext, psiClass);
        if (CollectionUtils.isEmpty(fuDocItemDataList)) {
            //发出通知 没有可以同步的接口
            return;
        }

        //4、获取api层级结构树
        ApiStructureTreeDTO apiStructureTree = getApiStructureTree(psiClass);

        //5、同步接口文档
        syncApiList(apiStructureTree, fuDocItemDataList, configData, fuDocContext.isSyncDialog());
    }


    /**
     * 同步接口文档
     *
     * @param apiStructureTree   第三方接口文档系统层级结构
     * @param fuDocItemDataList  同步的接口集合
     * @param baseSyncConfigData 第三方接口文档配置
     * @param enableDialog       同步接口文档至第三方系统时 之前同步的接口不在开启弹框
     */
    protected void syncApiList(ApiStructureTreeDTO apiStructureTree, List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData baseSyncConfigData, boolean enableDialog) {
        if (enableDialog) {
            //开启弹出开关（此处）
            Map<String, ApiTreeKeyDTO> apiTreeKeyMap = ObjectUtils.listToMap(buildApiPositionKey(apiStructureTree), ApiTreeKeyDTO::getApiTreeKey);
            //判断当前接口之前是否同步过 已经同步过的则不弹框
            List<SyncApiRecordData> syncRecordList = baseSyncConfigData.getSyncRecordList();
            Map<String, SyncApiRecordData> syncApiRecordDataMap = ObjectUtils.listToMap(syncRecordList, SyncApiRecordData::getApiKey);
            if (CollectionUtils.isNotEmpty(syncRecordList) && fuDocItemDataList.stream().allMatch(a -> isCanSync(syncApiRecordDataMap.get(a.getApiKey()), apiTreeKeyMap.keySet()))) {
                //无需开启弹框 直接加载之前同步记录的数据发起同步接口
                for (FuDocItemData fuDocItemData : fuDocItemDataList) {
                    //获取上一次同步的记录
                    SyncApiRecordData syncApiRecordData = syncApiRecordDataMap.get(fuDocItemData.getApiKey());
                    ApiTreeKeyDTO apiTreeKeyDTO = apiTreeKeyMap.get(buildApiTreeKey(syncApiRecordData));
                    //组装同步接口文档所需要的数据 同步接口
                    syncSingleApi(buildSyncApiData(apiTreeKeyDTO, fuDocItemData), baseSyncConfigData);
                }
                return;
            }
        }

        //弹框让用户选择需要同步的目录
        SyncApiView syncApiView = new SyncApiView(ProjectUtils.getCurrProject());
        if (!syncApiView.showAndGet()) {
            //选择取消 则不同步接口
            return;
        }
        List<SyncApiTableData> tableDataList = syncApiView.getTableDataList();
        if (CollectionUtils.isEmpty(tableDataList)) {
            //没有选择接口同步 直接返回
            return;
        }
        //获取用户选择好的接口循环同步
        for (SyncApiTableData syncApiTableData : tableDataList) {
            SyncApiData syncApiData = buildSyncApiData(syncApiTableData, syncApiTableData.getApiData());
            //同步接口文档至第三方接口文档系统
            boolean syncResult = syncSingleApi(syncApiData, baseSyncConfigData);
            //更新表格中每一条接口的同步结果
            syncApiTableData.setSyncStatus(syncResult ? "同步成功" : "同步失败");
        }
    }


    /**
     * 同步单个接口到第三方接口文档系统
     *
     * @param syncApiData 同步的接口数据
     * @param configData  第三方接口文档配置数据
     */
    protected boolean syncSingleApi(SyncApiData syncApiData, BaseSyncConfigData configData) {
        //5、组装第三方接口文档系统所需的数据结构
        String syncDaJson = assembleSyncData(syncApiData);

        //6、请求第三方接口文档系统接口 将接口文档同步至三方接口文档系统中
        String result = HttpUtil.post(configData.getBaseUrl() + configData.getSyncApiUrl(), syncDaJson, 6000);

        //7、校验返回结果
        String errorMsg = checkSyncResult(result);
        if (StringUtils.isNotBlank(errorMsg)) {
            FuDocNotification.notifyError(errorMsg);
            return false;
        }

        //8、新增同步记录
        List<SyncApiRecordData> syncRecordList = configData.getSyncRecordList();
        if (Objects.isNull(syncRecordList)) {
            syncRecordList = Lists.newArrayList();
            configData.setSyncRecordList(syncRecordList);
        }
        SyncApiRecordData recordData = new SyncApiRecordData();
        BeanUtil.copyProperties(syncApiData, recordData);
        recordData.setSyncTime(DatePattern.NORM_DATETIME_FORMAT.format(new Date()));
        recordData.setApiKey(syncApiData.getSyncApiData().getApiKey());
        syncRecordList.add(recordData);
        return true;
    }

    /**
     * 构建同步接口数据对象
     *
     * @param apiTreeKeyDTO 能够标识接口唯一位置的对象
     * @param fuDocItemData 接口数据
     * @param <T>           标识接口唯一位置的对象泛型
     * @return 同步接口文档的数据对象
     */
    private <T extends ApiTreeKeyDTO> SyncApiData buildSyncApiData(T apiTreeKeyDTO, FuDocItemData fuDocItemData) {
        SyncApiData syncApiData = new SyncApiData();
        BeanUtil.copyProperties(apiTreeKeyDTO, syncApiData);
        syncApiData.setSyncApiData(fuDocItemData);
        return syncApiData;
    }


    /**
     * 校验当前接口之前的同步记录现在是否可以同步（即判断之前同步记录的分组 项目 接口分类是否还存在 通过名称来校验）
     *
     * @param syncApiRecordData 之前同步的记录
     * @param apiTreeKeys       第三方接口文档系统现有的接口层级目录唯一标识集合
     * @return true 可以按照之前同步记录方式同步
     */
    private boolean isCanSync(SyncApiRecordData syncApiRecordData, Set<String> apiTreeKeys) {
        return Objects.nonNull(syncApiRecordData) && apiTreeKeys.contains(buildApiTreeKey(syncApiRecordData));
    }


    /**
     * 将api层级结构扁平化
     *
     * @param apiStructureTree api层级结构
     * @return 扁平化后的api结构
     */
    private List<ApiTreeKeyDTO> buildApiPositionKey(ApiStructureTreeDTO apiStructureTree) {
        List<ApiTreeKeyDTO> resultList = Lists.newArrayList();
        List<ApiGroupDTO> groupList = apiStructureTree.getGroupList();
        if (CollectionUtils.isNotEmpty(groupList)) {
            for (ApiGroupDTO apiGroupDTO : groupList) {
                List<ApiProjectDTO> apiProjectList = apiGroupDTO.getApiProjectList();
                if (CollectionUtils.isNotEmpty(apiProjectList)) {
                    for (ApiProjectDTO apiProjectDTO : apiProjectList) {
                        List<ApiCategoryDTO> categoryKeyList = buildApiCategoryKeyList(apiProjectDTO.getApiCategoryList());
                        if (CollectionUtils.isNotEmpty(categoryKeyList)) {
                            for (ApiCategoryDTO category : categoryKeyList) {
                                ApiTreeKeyDTO apiTreeKeyDTO = new ApiTreeKeyDTO();
                                apiTreeKeyDTO.setGroupId(apiGroupDTO.getGroupId());
                                apiTreeKeyDTO.setGroupName(apiGroupDTO.getGroupName());
                                apiTreeKeyDTO.setProjectId(apiProjectDTO.getProjectId());
                                apiTreeKeyDTO.setProjectName(apiProjectDTO.getProjectName());
                                apiTreeKeyDTO.setCategoryId(category.getCategoryId());
                                apiTreeKeyDTO.setCategoryName(category.getCategoryName());
                                resultList.add(apiTreeKeyDTO);
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }


    /**
     * 递归拼接接口分类 将接口分类名称通过"/"连接起来 将树形接口的api分类扁平化
     *
     * @param apiCategoryList 接口分类树集合
     * @return 扁平化后的接口分类集合
     */
    private List<ApiCategoryDTO> buildApiCategoryKeyList(List<ApiCategoryDTO> apiCategoryList) {
        List<ApiCategoryDTO> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(apiCategoryList)) {
            for (ApiCategoryDTO apiCategoryDTO : apiCategoryList) {
                String categoryName = apiCategoryDTO.getCategoryName();
                List<ApiCategoryDTO> categoryKeyList = buildApiCategoryKeyList(apiCategoryDTO.getApiCategoryList());
                if (CollectionUtils.isNotEmpty(categoryKeyList)) {
                    for (ApiCategoryDTO subCategory : categoryKeyList) {
                        resultList.add(new ApiCategoryDTO(subCategory.getCategoryId(), categoryName + "/" + subCategory));
                    }
                }
                return resultList;
            }
        }
        return apiCategoryList;
    }


    private String buildApiTreeKey(SyncApiRecordData syncApiRecordData) {
        return buildApiTreeKey(syncApiRecordData.getGroupName(), syncApiRecordData.getProjectName(), syncApiRecordData.getCategoryName());
    }

    /**
     * 构建一个接口的唯一位置
     *
     * @param groupName   分组名称
     * @param projectName 项目名称
     * @param category    分类名称（多个"/"拼接）
     * @return api树唯一key
     */
    private String buildApiTreeKey(String groupName, String projectName, String category) {
        return groupName + ":" + projectName + "" + category;
    }
}
