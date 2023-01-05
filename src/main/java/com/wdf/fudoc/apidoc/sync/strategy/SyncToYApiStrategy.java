package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.MockResultType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.data.YapiConfigData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.JsonUtil;
import com.wdf.fudoc.util.MapListUtil;
import com.wdf.fudoc.util.YApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    protected BaseSyncConfigData checkConfig(BaseSyncConfigData baseSyncConfigData) {
        if (Objects.isNull(baseSyncConfigData)) {
            baseSyncConfigData = new YapiConfigData();
        }
        //检查配置
        baseSyncConfigData.setBaseUrl("http://150.158.164.160:3000");
        baseSyncConfigData.setSyncApiUrl("/api/interface/save");
        baseSyncConfigData.setAddCategoryUrl("/api/interface/add_cat");
        return baseSyncConfigData;
    }

    @Override
    protected String assembleSyncData(SyncApiData syncApiData) {
        //组装Yapi需要的数据
        YApiSaveDTO convert = convert(syncApiData);
        return JsonUtil.toJson(convert);
    }

    @Override
    protected String checkSyncResult(SyncApiData syncApiData, String result) {
        //检查同步结果
        if (YApiUtil.isSuccess(result)) {
            return null;
        }
        String title = syncApiData.getFuDocItemData().getTitle();
        log.info("同步接口至YApi失败,YApi返回结果为:{}", result);
        return "同步接口【" + title + "】至YApi失败";
    }


    private YApiSaveDTO convert(SyncApiData syncApiData) {
        YApiSaveDTO yApiSaveDTO = new YApiSaveDTO();
        FuDocItemData fuDocItemData = syncApiData.getFuDocItemData();
        yApiSaveDTO.setToken(syncApiData.getProjectToken());
        String projectId = syncApiData.getProjectId();
        if (StringUtils.isNotBlank(projectId) && NumberUtil.isNumber(projectId)) {
            yApiSaveDTO.setProjectId(Long.valueOf(projectId));
        }
        String categoryId = syncApiData.getCategoryId();
        if (StringUtils.isNotBlank(categoryId) && NumberUtil.isNumber(categoryId)) {
            yApiSaveDTO.setCatId(Long.valueOf(categoryId));
        }
        yApiSaveDTO.setPath(fuDocItemData.getUrlList().get(0));
        yApiSaveDTO.setMethod(fuDocItemData.getRequestType());
        yApiSaveDTO.setReqBodyType(fuDocItemData.getContentType());
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
            jsonSchema.setProperties(buildProperties(instance.get(FuDocConstants.ROOT), instance));
        }
        return JsonUtil.toJson(jsonSchema);
    }


    private YApiJsonSchema buildJsonSchema(FuDocParamData fuDocParamData, MapListUtil<String, FuDocParamData> instance) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        String paramType = fuDocParamData.getParamType();
        if ("object".equals(paramType)) {
            //对象
            jsonSchema.setProperties(buildProperties(instance.get(fuDocParamData.getParamNo()), instance));
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


    private Map<String, YApiJsonSchema> buildProperties(List<FuDocParamData> childList, MapListUtil<String, FuDocParamData> instance) {
        Map<String, YApiJsonSchema> properties = new HashMap<>();
        if (CollectionUtils.isNotEmpty(childList)) {
            childList.forEach(f -> properties.put(f.getParamName(), buildJsonSchema(f, instance)));
        }
        return properties;
    }


    @Override
    public ApiStructureTreeDTO getApiStructureTree(PsiClass psiClass) {
        ApiStructureTreeDTO apiStructureTreeDTO = new ApiStructureTreeDTO();
        apiStructureTreeDTO.setCurrent(buildApiTreeKeyDTO());
        return apiStructureTreeDTO;
    }

    @Override
    public ApiCategoryDTO createCategory(BaseSyncConfigData baseSyncConfigData, AddApiCategoryDTO addApiCategoryDTO) {
        YApiCreateCategoryDTO categoryDTO = new YApiCreateCategoryDTO();
        categoryDTO.setToken(addApiCategoryDTO.getProjectToken());
        String projectId = addApiCategoryDTO.getProjectId();
        if (StringUtils.isNotBlank(projectId) && NumberUtil.isNumber(projectId)) {
            categoryDTO.setProjectId(Long.valueOf(projectId));
        }
        categoryDTO.setName(addApiCategoryDTO.getCategoryName());
        categoryDTO.setDesc(addApiCategoryDTO.getCategoryDesc());
        String result = HttpUtil.post(baseSyncConfigData.getBaseUrl() + baseSyncConfigData.getAddCategoryUrl(), JsonUtil.toJson(categoryDTO));
        YApiCreateCategoryDTO data = YApiUtil.getData(result, YApiCreateCategoryDTO.class);
        if (Objects.nonNull(data) && Objects.nonNull(data.getCatId())) {
            return new ApiCategoryDTO(data.getCatId() + "", data.getName());
        }
        return new ApiCategoryDTO();
    }


    private ApiProjectDTO buildApiTreeKeyDTO() {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setGroupId("11");
        apiProjectDTO.setGroupName("个人空间");
        apiProjectDTO.setProjectId("11");
        apiProjectDTO.setProjectName("test2");
        apiProjectDTO.setProjectToken("4fb217c42b162c2b250041cbc5edc76a66c3c3bd6c5896fbb7941ef8198f09b8");
        return apiProjectDTO;
    }
}
