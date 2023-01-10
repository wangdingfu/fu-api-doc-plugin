package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.*;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.data.YapiConfigData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.sync.service.YApiService;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 同步接口文档到Yapi接口文档系统的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:47:39
 */
@Slf4j
public class SyncToYApiStrategy extends AbstractSyncFuDocStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData baseSyncConfigData) {
        return true;
    }

    @Override
    protected String doSync(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        YApiSaveDTO yApiSaveDTO = assembleSyncData(fuDocItemData, apiProjectDTO, apiCategoryDTO);
        YApiService service = ServiceHelper.getService(YApiService.class);
        try {
            if (!service.saveOrUpdate(configData.getBaseUrl(), yApiSaveDTO)) {
                return "同步至YApi失败";
            }
        } catch (Exception e) {
            return "同步至YApi失败";
        }
        return null;
    }

    @Override
    protected ApiProjectDTO getSyncProjectConfig(BaseSyncConfigData configData, PsiClass psiClass) {
        YapiConfigData yapiConfigData = (YapiConfigData) configData;
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);

        List<ApiProjectDTO> projectConfigList = yapiConfigData.getProjectConfigList(module.getName());
        if (CollectionUtils.isNotEmpty(projectConfigList)) {
            ApiProjectDTO apiProjectDTO = projectConfigList.get(0);
            apiProjectDTO.setModuleName(module.getName());
            return apiProjectDTO;
        }
        return null;
    }

    protected YApiSaveDTO assembleSyncData(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        //组装Yapi需要的数据
        return convert(fuDocItemData, apiProjectDTO, apiCategoryDTO);
    }

    protected String checkSyncResult(SyncApiData syncApiData, String result) {
        //检查同步结果
        if (YApiUtil.isSuccess(result)) {
            return null;
        }
        String title = syncApiData.getFuDocItemData().getTitle();
        log.info("同步接口至YApi失败,YApi返回结果为:{}", result);
        return "同步接口【" + title + "】至YApi失败";
    }


    private YApiSaveDTO convert(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        YApiSaveDTO yApiSaveDTO = new YApiSaveDTO();
        yApiSaveDTO.setToken(apiProjectDTO.getProjectToken());
        String projectId = apiProjectDTO.getProjectId();
        if (StringUtils.isNotBlank(projectId) && NumberUtil.isNumber(projectId)) {
            yApiSaveDTO.setProjectId(Long.valueOf(projectId));
        }
        String categoryId = apiCategoryDTO.getCategoryId();
        if (StringUtils.isNotBlank(categoryId) && NumberUtil.isNumber(categoryId)) {
            yApiSaveDTO.setCatId(Long.valueOf(categoryId));
        }
        yApiSaveDTO.setPath(fuDocItemData.getUrlList().get(0));
        yApiSaveDTO.setMethod(fuDocItemData.getRequestType());
        String contentType = fuDocItemData.getContentType();
        yApiSaveDTO.setReqBodyType(StringUtils.isBlank(contentType) ? ContentType.FORM_DATA.getDesc() : contentType);
        if (MockResultType.JSON.getCode().equals(fuDocItemData.getRequestExampleType())) {
            //post请求 填充json schema
            yApiSaveDTO.setReqBodyOther(buildJsonSchema(fuDocItemData.getRequestParams()));
            yApiSaveDTO.setReqBodyIsJsonSchema(true);
        } else {
            List<FuDocParamData> requestParams = fuDocItemData.getRequestParams();
            yApiSaveDTO.setReqBodyIsJsonSchema(false);
            yApiSaveDTO.setReqParams(buildParams(filterUrlParams(requestParams)));
            List<YApiParamDTO> yApiParamDTOList = buildParams(filterRequestParams(requestParams));
            if (RequestType.GET.getRequestType().equals(fuDocItemData.getRequestType())) {
                yApiSaveDTO.setReqQuery(yApiParamDTOList);
            } else {
                yApiSaveDTO.setReqBodyForm(yApiParamDTOList);
            }
        }
        //设置请求头
        yApiSaveDTO.setReqHeaders(Lists.newArrayList());
        //响应数据的json schema
        yApiSaveDTO.setResBody(buildJsonSchema(fuDocItemData.getResponseParams()));
        yApiSaveDTO.setTitle(fuDocItemData.getTitle());

        //设置markdown内容
        yApiSaveDTO.setMarkdown(FuDocRender.yapiMarkdown(fuDocItemData, FuDocSetting.getSettingData()));
        yApiSaveDTO.setDesc(MarkdownToHtmlUtils.markdownToHtml(yApiSaveDTO.getMarkdown()));
        return yApiSaveDTO;

    }


    private List<FuDocParamData> filterUrlParams(List<FuDocParamData> requestParams) {
        if (CollectionUtils.isNotEmpty(requestParams)) {
            return requestParams.stream().filter(f -> f.getFudoc().containsKey(FuDocConstants.PATH_VARIABLE)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private List<FuDocParamData> filterRequestParams(List<FuDocParamData> requestParams) {
        if (CollectionUtils.isNotEmpty(requestParams)) {
            return requestParams.stream().filter(f -> !f.getFudoc().containsKey(FuDocConstants.PATH_VARIABLE)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }


    private List<YApiParamDTO> buildParams(List<FuDocParamData> paramList) {
        List<YApiParamDTO> yApiParamDTOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (FuDocParamData fuDocParamData : paramList) {
                YApiParamDTO yApiParamDTO = new YApiParamDTO();
                yApiParamDTO.setName(fuDocParamData.getParamName());
                RequestParamType requestParamType = "file".equals(fuDocParamData.getParamType()) ? RequestParamType.FILE : RequestParamType.TEXT;
                yApiParamDTO.setType(requestParamType.getCode());
                yApiParamDTO.setRequired(YesOrNo.getCode(fuDocParamData.getParamRequire()) + "");
                yApiParamDTO.setExample(fuDocParamData.getParamValue());
                yApiParamDTO.setDesc(fuDocParamData.getParamDesc());
                yApiParamDTOList.add(yApiParamDTO);
            }
        }
        return yApiParamDTOList;
    }


    /**
     * 构建json schema
     *
     * @param fuDocParamDataList 接口文档参数集合
     * @return json schema
     */
    private String buildJsonSchema(List<FuDocParamData> fuDocParamDataList) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        jsonSchema.setType("object");
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            MapListUtil<String, FuDocParamData> instance = MapListUtil.getInstance(fuDocParamDataList, FuDocParamData::getParentParamNo);
            YApiJsonSchema yApiJsonSchema = buildProperties(instance.get(FuDocConstants.ROOT), instance);
            jsonSchema.setProperties(yApiJsonSchema.getProperties());
            jsonSchema.setRequired(yApiJsonSchema.getRequired());
        }
        return JsonUtil.toJson(jsonSchema);
    }


    private YApiJsonSchema buildJsonSchema(FuDocParamData fuDocParamData, MapListUtil<String, FuDocParamData> instance) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        String paramType = fuDocParamData.getParamType();
        if ("object".equals(paramType)) {
            //对象
            YApiJsonSchema yApiJsonSchema = buildProperties(instance.get(fuDocParamData.getParamNo()), instance);
            jsonSchema.setProperties(yApiJsonSchema.getProperties());
            jsonSchema.setRequired(yApiJsonSchema.getRequired());
        } else if ("array".equals(paramType)) {
            //组装items
            FuDocParamData item = new FuDocParamData();
            item.setParamNo(fuDocParamData.getParamNo());
            item.setParamDesc(fuDocParamData.getParamDesc());
            item.setParamValue(fuDocParamData.getParamValue());
            item.setParamType(fuDocParamData.getChildParamType());
            jsonSchema.setItems(buildJsonSchema(item, instance));
        } else {
            String paramValue = fuDocParamData.getParamValue();
            if (StringUtils.isNotBlank(paramValue)) {
                jsonSchema.setMock(new YApiMock(paramValue));
            }
        }
        jsonSchema.setType(paramType);
        jsonSchema.setDescription(fuDocParamData.getParamDesc());
        return jsonSchema;
    }


    private YApiJsonSchema buildProperties(List<FuDocParamData> childList, MapListUtil<String, FuDocParamData> instance) {
        YApiJsonSchema result = new YApiJsonSchema();
        List<String> required = Lists.newArrayList();
        Map<String, YApiJsonSchema> properties = new HashMap<>();
        if (CollectionUtils.isNotEmpty(childList)) {
            childList.forEach(f -> {
                properties.put(f.getParamName(), buildJsonSchema(f, instance));
                if(YesOrNo.YES.getDesc().equals(f.getParamRequire())){
                    required.add(f.getParamName());
                }
            });
        }
        result.setProperties(properties);
        result.setRequired(required);
        return result;
    }


    @Override
    public ApiCategoryDTO createCategory(BaseSyncConfigData baseSyncConfigData, ApiProjectDTO apiProjectDTO, String categoryName) {
        YApiCreateCategoryDTO categoryDTO = new YApiCreateCategoryDTO();
        categoryDTO.setToken(apiProjectDTO.getProjectToken());
        String projectId = apiProjectDTO.getProjectId();
        if (StringUtils.isNotBlank(projectId) && NumberUtil.isNumber(projectId)) {
            categoryDTO.setProjectId(Long.valueOf(projectId));
        }
        categoryDTO.setName(categoryName);
        YApiService service = ServiceHelper.getService(YApiService.class);
        return service.createCategory(baseSyncConfigData.getBaseUrl(), categoryDTO);
    }

    @Override
    public List<ApiCategoryDTO> categoryList(ApiProjectDTO apiProjectDTO, BaseSyncConfigData baseSyncConfigData) {
        YApiService service = ServiceHelper.getService(YApiService.class);
        return service.categoryList(baseSyncConfigData.getBaseUrl(), apiProjectDTO.getProjectId(), apiProjectDTO.getProjectToken());
    }

}
