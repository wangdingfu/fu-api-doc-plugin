package com.wdf.fudoc.request.data;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.constants.enumtype.HeaderScope;
import com.wdf.fudoc.request.pojo.CommonHeader;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-12-28 22:38:35
 */
@Getter
@Setter
public class FuRequestSettingData {


    /**
     * 公共请求头持久化
     */
    private List<CommonHeader> commonHeaderList = Lists.newArrayList();


    public List<CommonHeader> getCommonHeaderList() {
        if (CollectionUtils.isNotEmpty(this.commonHeaderList)) {
            List<CommonHeader> commonHeaders = Lists.newArrayList(this.commonHeaderList);
            for (CommonHeader commonHeader : commonHeaders) {
                if (HeaderScope.ALL_PROJECT.getName().equals(commonHeader.getScope())) {
                    Boolean select = commonHeader.getSelect();
                    if (Objects.isNull(select) || select) {
                        commonHeader.setSelect(true);
                    }
                } else {
                    List<String> projectIdList = commonHeader.getProjectIdList();
                    if (CollectionUtils.isNotEmpty(projectIdList)) {
                        Project currProject = ProjectUtils.getCurrProject();
                        String locationHash = currProject.getLocationHash();
                        commonHeader.setSelect(projectIdList.contains(locationHash));
                    }
                }
            }
            return commonHeaders;
        }
        return this.commonHeaderList;
    }
}
