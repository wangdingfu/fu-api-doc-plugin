package com.wdf.fudoc.components.bo;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.api.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-08 16:34:05
 */
@Getter
@Setter
public class TreePathBO implements Serializable {

    private List<String> selectPathList;

    private String view;

    public TreePathBO() {
    }

    public TreePathBO(List<String> selectPathList) {
        this.selectPathList = selectPathList;
        this.view = FuStringUtils.join(selectPathList, ",");

    }

    public boolean isNotNull() {
        return FuStringUtils.isNotBlank(this.view) && CollectionUtils.isNotEmpty(selectPathList);
    }

    public boolean isScope(Module module) {
        if (Objects.isNull(module) || CollectionUtils.isEmpty(selectPathList)) {
            //没有指定范围 则默认全部有效
            return true;
        }
        if (selectPathList.contains(module.getName())) {
            return true;
        }
        Project project = module.getProject();
        String name = project.getName();
        return selectPathList.contains(name);
    }

    public boolean isScope(String moduleName) {
        if (FuStringUtils.isBlank(moduleName) || CollectionUtils.isEmpty(selectPathList)) {
            //没有指定范围 则默认全部有效
            return true;
        }
        if (selectPathList.contains(moduleName)) {
            return true;
        }
        Project currProject = ProjectUtils.getCurrProject();
        return selectPathList.contains(currProject.getName());
    }



    private static final Integer DEFAULT_SORT = 99;
    private static final Integer ONE_LEVEL_SORT = 1;
    private static final Integer NOT_MATCH = 0;

    /**
     * 根据module名称来选中的module树匹配 如果没有匹配成功 则说明没有该module的配置 匹配成功按照优先级返回顺序
     *
     * @param moduleName module名称
     * @return 0-没有匹配 其他-按照升序体现优先级
     */
    public Integer matchModule(String moduleName) {
        if (FuStringUtils.isBlank(moduleName)) {
            return DEFAULT_SORT;
        }
        if (this.view.equals(moduleName)) {
            //传入的module正好匹配 优先级最高
            return ONE_LEVEL_SORT;
        }
        Project currProject = ProjectUtils.getCurrProject();
        String currProjectName = currProject.getName();
        if (this.view.equals(currProjectName)) {
            //当前配置指定的是针对当前所有项目 则不用继续匹配 当前项目所有模块均可以同步
            return ONE_LEVEL_SORT;
        }
        for (String path : selectPathList) {
            //选中的节点 循环是因为可能会存在多选的情况
            if (FuStringUtils.isNotBlank(path) && (moduleName.equals(path) || currProjectName.equals(path))) {
                return ONE_LEVEL_SORT;
            }
        }
        //没有匹配到
        return NOT_MATCH;
    }

    @Override
    public String toString() {
        return FuStringUtils.isNotBlank(this.view) ? this.view : FuStringUtils.EMPTY;
    }
}
