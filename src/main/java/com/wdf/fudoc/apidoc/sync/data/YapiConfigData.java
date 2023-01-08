package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
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

    @Override
    public List<String> getProjectNameList(String moduleName) {
        return ObjectUtils.listToList(projectConfigList, YApiProjectTableData::getProjectName);
    }


    public YApiProjectTableData getByProjectName(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            return null;
        }
        return projectConfigList.stream().filter(f -> f.getProjectName().equals(projectName)).findFirst().orElse(null);
    }
}
