package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Yapi配置
 *
 * @author wangdingfu
 * @date 2023-01-05 11:52:04
 */
@Getter
@Setter
public class YapiConfigData extends BaseSyncConfigData {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 项目配置
     */
    private List<YApiProjectTableData> projectConfigList = Lists.newArrayList();



    private ApiProjectDTO convert(YApiProjectTableData tableData){
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectToken(tableData.getProjectToken());
        apiProjectDTO.setProjectId(tableData.getProjectId());
        apiProjectDTO.setProjectName(tableData.getProjectName());
        return apiProjectDTO;
    }


    @Override
    public List<ApiProjectDTO> getProjectConfigList(String moduleName) {
        return ObjectUtils.listToList(projectConfigList, this::convert);
    }
}
