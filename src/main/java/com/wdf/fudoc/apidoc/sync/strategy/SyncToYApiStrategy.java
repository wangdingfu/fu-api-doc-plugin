package com.wdf.fudoc.apidoc.sync.strategy;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.*;
import com.wdf.fudoc.apidoc.helper.JsonSchemaHelper;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.*;
import com.wdf.fudoc.apidoc.sync.service.YApiService;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 同步接口文档到Yapi接口文档系统的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:47:39
 */
@Slf4j
public class SyncToYApiStrategy extends AbstractSyncSingleApiStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData baseSyncConfigData) {
        //do nothing
        return false;
    }


    /**
     * 同步接口文档至YApi系统
     *
     * @param configData     Yapi系统配置数据
     * @param fuDocItemData  接口文档
     * @param apiProjectDTO  同步到指定的项目
     * @param apiCategoryDTO 同步到指定的分类
     * @return 同步失败消息
     */
    @Override
    protected String doSingleApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        if (Objects.isNull(apiProjectDTO) || org.apache.commons.lang.StringUtils.isBlank(apiProjectDTO.getProjectToken()) || org.apache.commons.lang.StringUtils.isBlank(apiProjectDTO.getProjectId())) {
            //构建返回结果
            throw new FuDocException("同步的项目数据错误");
        }
        if (Objects.isNull(apiCategoryDTO) || org.apache.commons.lang.StringUtils.isBlank(apiCategoryDTO.getCategoryId()) || org.apache.commons.lang.StringUtils.isBlank(apiCategoryDTO.getCategoryName())) {
            //构建返回结果
            throw new FuDocException("同步的分类数据错误");
        }
        //构建同步至YApi系统的数据
        YApiSaveDTO yApiSaveDTO = buildYApiSaveDTO(fuDocItemData, apiProjectDTO, apiCategoryDTO);
        YApiService service = ServiceHelper.getService(YApiService.class);
        return service.saveOrUpdate(configData.getBaseUrl(), yApiSaveDTO);
    }


    /**
     * 创建一个接口分类
     *
     * @param baseSyncConfigData 接口文档配置
     * @param apiProjectDTO      指定的项目
     * @param categoryName       分类名称
     * @return 创建成功的分类对象
     */
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


    /**
     * 查询指定项目下的接口分类列表
     *
     * @param apiProjectDTO      项目名称
     * @param baseSyncConfigData 配置数据
     * @return 指定项目下的接口分类列表
     */
    @Override
    public List<ApiCategoryDTO> categoryList(ApiProjectDTO apiProjectDTO, BaseSyncConfigData baseSyncConfigData) {
        YApiService service = ServiceHelper.getService(YApiService.class);
        return service.categoryList(baseSyncConfigData.getBaseUrl(), apiProjectDTO.getProjectId(), apiProjectDTO.getProjectToken());
    }


    /**
     * 构建YApi接收的数据格式
     *
     * @param fuDocItemData  接口文档
     * @param apiProjectDTO  选中的项目
     * @param apiCategoryDTO 选中的分类
     * @return YApi接收的数据格式
     */
    private YApiSaveDTO buildYApiSaveDTO(FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        YApiSaveDTO yApiSaveDTO = new YApiSaveDTO();
        //项目token 将接口同步至该项目下
        yApiSaveDTO.setToken(apiProjectDTO.getProjectToken());
        String projectId = apiProjectDTO.getProjectId();
        if (StringUtils.isNotBlank(projectId) && NumberUtil.isNumber(projectId)) {
            yApiSaveDTO.setProjectId(Long.valueOf(projectId));
        }
        //接口分类设置 将接口同步至该分类下
        String categoryId = apiCategoryDTO.getCategoryId();
        if (StringUtils.isNotBlank(categoryId) && NumberUtil.isNumber(categoryId)) {
            yApiSaveDTO.setCatId(Long.valueOf(categoryId));
        }
        //接口url
        yApiSaveDTO.setPath(fuDocItemData.getUrlList().get(0));
        yApiSaveDTO.setMethod(fuDocItemData.getRequestType());
        //接口请求body类型 GET请求默认设置为form POST请求需要根据实际请求内容决定
        ContentType contentType = fuDocItemData.getContentType();
        yApiSaveDTO.setReqBodyType(Objects.isNull(contentType) ? ContentType.FORM_DATA.getDesc() : contentType.getDesc());
        if (MockResultType.JSON.getCode().equals(fuDocItemData.getRequestExampleType())) {
            //请求内容为JSON格式 填充json schema
            yApiSaveDTO.setReqBodyOther(JsonSchemaHelper.buildJsonSchemaStr(fuDocItemData.getRequestParams()));
            yApiSaveDTO.setReqBodyIsJsonSchema(true);
        } else {
            //请求内容不是JSON(目前暂未考虑raw格式) 如果是GET请求 填充成查询参数 如果不是则填充到form参数中
            List<FuDocParamData> requestParams = fuDocItemData.getRequestParams();
            yApiSaveDTO.setReqBodyIsJsonSchema(false);
            yApiSaveDTO.setReqParams(buildParams(filterPathVariableParams(requestParams)));
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
        yApiSaveDTO.setResBody(JsonSchemaHelper.buildJsonSchemaStr(fuDocItemData.getResponseParams()));
        yApiSaveDTO.setTitle(fuDocItemData.getTitle());

        //设置markdown内容
        yApiSaveDTO.setMarkdown(FuDocRender.yapiMarkdown(fuDocItemData, FuDocSetting.getSettingData()));
        yApiSaveDTO.setDesc(MarkdownToHtmlUtils.markdownToHtml(yApiSaveDTO.getMarkdown()));
        return yApiSaveDTO;

    }


    /**
     * 过滤PathVariable格式的参数
     *
     * @param requestParams 所有的请求参数
     * @return PathVariable格式的参数
     */
    private List<FuDocParamData> filterPathVariableParams(List<FuDocParamData> requestParams) {
        if (Objects.nonNull(requestParams) && requestParams.size() > 0) {
            return requestParams.stream().filter(f -> f.getExt().containsKey(FuDocConstants.PATH_VARIABLE)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 过滤不是PathVariable格式的参数
     *
     * @param requestParams 所有的请求参数
     * @return 不是PathVariable格式的参数
     */
    private List<FuDocParamData> filterRequestParams(List<FuDocParamData> requestParams) {
        if (Objects.nonNull(requestParams) && requestParams.size() > 0) {
            return requestParams.stream().filter(f -> !f.getExt().containsKey(FuDocConstants.PATH_VARIABLE)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }


    /**
     * 构建YApi接收的参数格式
     *
     * @param paramList 参数集合
     * @return YApi需要的参数格式集合
     */
    private List<YApiParamDTO> buildParams(List<FuDocParamData> paramList) {
        List<YApiParamDTO> yApiParamDTOList = Lists.newArrayList();
        if (Objects.nonNull(paramList) && paramList.size() > 0) {
            for (FuDocParamData fuDocParamData : paramList) {
                YApiParamDTO yApiParamDTO = new YApiParamDTO();
                yApiParamDTO.setName(fuDocParamData.getParamName());
                RequestParamType requestParamType = RequestParamType.FILE.getCode().equals(fuDocParamData.getParamType()) ? RequestParamType.FILE : RequestParamType.TEXT;
                yApiParamDTO.setType(requestParamType.getCode());
                yApiParamDTO.setRequired(YesOrNo.getCode(fuDocParamData.getParamRequire()) + "");
                yApiParamDTO.setExample(fuDocParamData.getParamValue());
                yApiParamDTO.setDesc(fuDocParamData.getParamDesc());
                yApiParamDTOList.add(yApiParamDTO);
            }
        }
        return yApiParamDTOList;
    }



}
