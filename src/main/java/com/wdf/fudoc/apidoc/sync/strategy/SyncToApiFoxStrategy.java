package com.wdf.fudoc.apidoc.sync.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.helper.JsonSchemaHelper;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxConfigData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.sync.service.ApiFoxService;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiConfirmDialog;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ApiFox同步文档策略
 *
 * @author wangdingfu
 * @date 2023-06-12 21:39:05
 */
public class SyncToApiFoxStrategy extends AbstractSyncApiStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData configData) {
        ApiFoxConfigData apiFoxConfigData = (ApiFoxConfigData) configData;
        String token = apiFoxConfigData.getToken();
        return StringUtils.isBlank(token);
    }

    /**
     * 同步api至ApiFox系统
     *
     * @param fuDocItemDataList 接口文档api集合
     * @param configData        配置数据
     * @param apiProjectDTO     同步项目
     * @param projectRecord     同步记录
     * @return 同步结果
     */
    @Override
    protected List<SyncApiResultDTO> doSyncApi(List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData projectRecord) {
        ApiFoxDTO apiFoxDTO = new ApiFoxDTO();
        OpenApiDTO openApiDTO = new OpenApiDTO();
        apiFoxDTO.setApiOverwriteMode("methodAndPath");
        apiFoxDTO.setImportFormat("openapi");
        apiFoxDTO.setSyncApiFolder(true);
        apiFoxDTO.setData(openApiDTO);
        Map<String, Map<String, OpenApiItemDTO>> paths = new HashMap<>();
        for (FuDocItemData fuDocItemData : fuDocItemDataList) {
            Map<String, OpenApiItemDTO> itemMap = new HashMap<>();
            itemMap.put(fuDocItemData.getRequestType().toLowerCase(), buildOpenApiItem(fuDocItemData, apiProjectDTO));
            paths.put(fuDocItemData.getUrlList().get(0), itemMap);
        }
        openApiDTO.setPaths(paths);
        ApiFoxService service = ServiceHelper.getService(ApiFoxService.class);
        //同步api
        String errorMsg = service.syncApi(apiFoxDTO, apiProjectDTO, (ApiFoxConfigData) configData);
        ApiSyncStatus syncStatus = StringUtils.isBlank(errorMsg) ? ApiSyncStatus.SUCCESS : ApiSyncStatus.FAIL;
        return ObjectUtils.listToList(fuDocItemDataList, f -> buildSyncApiResult(f, apiProjectDTO, syncStatus, errorMsg));
    }


    private SyncApiResultDTO buildSyncApiResult(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiSyncStatus apiSyncStatus, String errorMsg) {
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


    /**
     * 确认需要同步的分类
     *
     * @param apiProjectDTO 同步的项目
     * @param configData    配置数据
     * @param psiClass      当前api所在的java类
     * @param projectRecord 同步记录
     * @return api同步至哪个分类下
     */
    @Override
    protected ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
        SyncApiConfirmDialog syncApiConfirmDialog = new SyncApiConfirmDialog(ProjectUtils.getCurrProject(), psiClass);
        if (!syncApiConfirmDialog.showAndGet()) {
            //取消则不同步
            return null;
        }
        return syncApiConfirmDialog.getSelected();
    }


    private OpenApiItemDTO buildOpenApiItem(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO) {
        OpenApiItemDTO openApiItemDTO = new OpenApiItemDTO();
        openApiItemDTO.setSummary(fuDocItemData.getTitle());
        openApiItemDTO.setStatus("released");
        openApiItemDTO.setDescription(fuDocItemData.getDetailInfo());
        openApiItemDTO.setFolder(recursionPath(apiProjectDTO.getSelectCategory()));
        openApiItemDTO.setParameters(buildParameters(fuDocItemData));
        openApiItemDTO.setRequestBody(buildRequestBody(fuDocItemData));
        Map<String, OpenApiResponseDTO> response = new HashMap<>();
        response.put("200", buildApiResponse(fuDocItemData));
        openApiItemDTO.setResponses(response);
        return openApiItemDTO;
    }


    private String recursionPath(ApiCategoryDTO apiCategoryDTO) {
        if (Objects.isNull(apiCategoryDTO)) {
            return StringUtils.EMPTY;
        }
        String parentName = recursionPath(apiCategoryDTO.getParent());
        String categoryName = apiCategoryDTO.getCategoryName();
        return StringUtils.isBlank(parentName) ? categoryName : parentName + "/" + categoryName;
    }


    private List<OpenApiParameterItemDTO> buildParameters(FuDocItemData fuDocItemData) {
        String requestType = fuDocItemData.getRequestType();
        List<FuDocParamData> requestParams = fuDocItemData.getRequestParams();
        if (RequestType.GET.getRequestType().equals(requestType)) {
            //GET请求参数组装
            return requestParams.stream().map(m -> buildParameterItem(m, m.getExt().containsKey(FuDocConstants.PATH_VARIABLE) ? "path" : "query")).collect(Collectors.toList());
        }
        //主要是解析@PathVariable上的参数返回
        return filterPathVariableParams(requestParams).stream().map(m -> buildParameterItem(m, "path")).collect(Collectors.toList());
    }

    private OpenApiParameterItemDTO buildParameterItem(FuDocParamData fuDocParamData, String in) {
        OpenApiParameterItemDTO parameterItemDTO = new OpenApiParameterItemDTO();
        parameterItemDTO.setName(fuDocParamData.getParamName());
        parameterItemDTO.setIn(in);
        parameterItemDTO.setDescription(fuDocParamData.getParamDesc());
        parameterItemDTO.setRequired(YesOrNo.YES.getDesc().equals(fuDocParamData.getParamRequire()));
        parameterItemDTO.setExample(fuDocParamData.getParamValue());
        YApiJsonSchema schema = new YApiJsonSchema();
        schema.setType(fuDocParamData.getParamType());
        parameterItemDTO.setSchema(schema);
        return parameterItemDTO;
    }

    private OpenApiResponseDTO buildApiResponse(FuDocItemData fuDocItemData) {
        OpenApiResponseDTO apiResponseDTO = new OpenApiResponseDTO();
        apiResponseDTO.setContent(buildContent(fuDocItemData, true));
        apiResponseDTO.setDescription("OK");
        return apiResponseDTO;
    }


    private OpenApiRequestBody buildRequestBody(FuDocItemData fuDocItemData) {
        OpenApiRequestBody requestBody = new OpenApiRequestBody();
        requestBody.setContent(buildContent(fuDocItemData, false));
        return requestBody;
    }

    private Map<String, OpenApiContentDTO> buildContent(FuDocItemData fuDocItemData, boolean isResponse) {
        String requestType = fuDocItemData.getRequestType();
        Map<String, OpenApiContentDTO> content = new HashMap<>();
        if (RequestType.POST.getRequestType().equals(requestType)) {
            ContentType contentType = fuDocItemData.getContentType();
            if (Objects.isNull(contentType)) {
                contentType = ContentType.JSON;
            }
            OpenApiContentDTO openApiContentDTO = new OpenApiContentDTO();
            openApiContentDTO.setExample(isResponse ? fuDocItemData.getResponseExample() : fuDocItemData.getRequestExample());
            openApiContentDTO.setSchema(JsonSchemaHelper.buildJsonSchema(isResponse ? fuDocItemData.getResponseParams() : fuDocItemData.getRequestParams()));
            content.put(contentType.getType(), openApiContentDTO);
        }
        return content;
    }

    /**
     * 过滤PathVariable格式的参数
     *
     * @param requestParams 所有的请求参数
     * @return PathVariable格式的参数
     */
    private List<FuDocParamData> filterPathVariableParams(List<FuDocParamData> requestParams) {
        if (CollectionUtils.isNotEmpty(requestParams)) {
            return requestParams.stream().filter(f -> f.getExt().containsKey(FuDocConstants.PATH_VARIABLE)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

}
