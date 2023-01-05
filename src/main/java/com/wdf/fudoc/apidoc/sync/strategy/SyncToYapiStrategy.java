package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONNull;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.MockResultType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.JsonUtil;
import com.wdf.fudoc.util.MapListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步接口文档到Yapi接口文档系统的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:47:39
 */
public class SyncToYapiStrategy extends AbstractSyncFuDocStrategy {


    @Override
    protected void checkConfig(BaseSyncConfigData baseSyncConfigData) {
        //检查配置
        baseSyncConfigData.setBaseUrl("http://150.158.164.160:3000");
        baseSyncConfigData.setSyncApiUrl("/api/interface/save");
    }

    @Override
    protected String assembleSyncData(SyncApiData syncApiData) {
        //组装Yapi需要的数据
        YApiSaveDTO convert = convert(syncApiData);
        return JsonUtil.toJson(convert);
    }


    private YApiSaveDTO convert(SyncApiData syncApiData) {
        YApiSaveDTO yApiSaveDTO = new YApiSaveDTO();
        FuDocItemData fuDocItemData = syncApiData.getFuDocItemData();
        yApiSaveDTO.setToken(syncApiData.getProjectToken());
        yApiSaveDTO.setProjectId(NumberUtil.binaryToLong(syncApiData.getProjectId()));
        yApiSaveDTO.setCatId(NumberUtil.binaryToLong(syncApiData.getCategoryId()));
        yApiSaveDTO.setPath(fuDocItemData.getUrlList().get(0));
        yApiSaveDTO.setMethod(fuDocItemData.getRequestType());
        String contentType = fuDocItemData.getContentType();
        yApiSaveDTO.setReqBodyType(contentType);
        if (MockResultType.JSON.getCode().equals(fuDocItemData.getRequestExampleType())) {
            //post请求 填充json schema
            yApiSaveDTO.setReqBodyOther(buildJsonSchema(fuDocItemData.getRequestParams()));
            yApiSaveDTO.setReqBodyIsJsonSchema(true);
        } else {
            yApiSaveDTO.setReqBodyIsJsonSchema(false);
            yApiSaveDTO.setReqParams(buildParams(fuDocItemData.getUrlParams()));
            List<YApiParamDTO> yApiParamDTOList = buildParams(fuDocItemData.getRequestParams());
            if (RequestType.GET.getRequestType().equals(fuDocItemData.getRequestType())) {
                yApiSaveDTO.setReqQuery(yApiParamDTOList);
            } else {
                yApiSaveDTO.setReqBodyForm(yApiParamDTOList);
            }
        }
        //设置请求头

        //响应数据的json schema
        yApiSaveDTO.setResBody(buildJsonSchema(fuDocItemData.getResponseParams()));
        yApiSaveDTO.setTitle(fuDocItemData.getTitle());
        return yApiSaveDTO;
    }


    private List<YApiParamDTO> buildParams(List<FuDocParamData> paramList) {
        List<YApiParamDTO> yApiParamDTOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (FuDocParamData fuDocParamData : paramList) {
                YApiParamDTO yApiParamDTO = new YApiParamDTO();
                yApiParamDTO.setName(fuDocParamData.getParamName());
                yApiParamDTO.setType(fuDocParamData.getParamType());
                yApiParamDTO.setRequired(fuDocParamData.getParamRequire());
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
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            MapListUtil<String, FuDocParamData> instance = MapListUtil.getInstance(fuDocParamDataList, FuDocParamData::getParentParamNo);
            Map<String, YApiJsonSchema> properties = new HashMap<>();
            List<FuDocParamData> rootList = instance.get(FuDocConstants.ROOT);
            if (CollectionUtils.isNotEmpty(rootList)) {
                rootList.forEach(f -> properties.put(f.getParamName(), buildJsonSchema(f, instance.get(f.getParamNo()))));
            }
            jsonSchema.setProperties(properties);
        }
        return JsonUtil.toJson(jsonSchema);
    }


    private YApiJsonSchema buildJsonSchema(FuDocParamData fuDocParamData, List<FuDocParamData> childList) {
        YApiJsonSchema jsonSchema = new YApiJsonSchema();
        String paramType = fuDocParamData.getParamType();
        if ("object".equals(paramType)) {
            //对象
            jsonSchema.setProperties();
        }

    }


    @Override
    protected String checkSyncResult(String result) {
        //检查同步结果

        return null;
    }

    @Override
    public ApiStructureTreeDTO getApiStructureTree(PsiClass psiClass) {
        ApiStructureTreeDTO apiStructureTreeDTO = new ApiStructureTreeDTO();
        apiStructureTreeDTO.setCurrent(buildApiTreeKeyDTO());
        return apiStructureTreeDTO;
    }

    @Override
    public ApiCategoryDTO createCategory(ApiProjectDTO apiProjectDTO) {
        //创建分类
        return null;
    }

    private ApiProjectDTO buildApiTreeKeyDTO() {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setGroupId("11");
        apiProjectDTO.setGroupName("个人空间");
        apiProjectDTO.setProjectId("20");
        apiProjectDTO.setProjectName("test2");
        return apiProjectDTO;
    }
}
