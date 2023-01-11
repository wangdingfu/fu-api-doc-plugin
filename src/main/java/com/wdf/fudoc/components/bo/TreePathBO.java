package com.wdf.fudoc.components.bo;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.tree.TreePath;
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

    private TreePath[] selectPath;

    private String view;

    public TreePathBO(TreePath[] selectPath) {
        this.selectPath = selectPath;
        if (Objects.nonNull(selectPath) && selectPath.length > 0) {
            List<String> viewNameList = Lists.newArrayList();
            for (TreePath treePath : selectPath) {
                Object lastPathComponent = treePath.getLastPathComponent();
                if (Objects.nonNull(lastPathComponent)) {
                    viewNameList.add(lastPathComponent.toString());
                }
            }
            this.view = StringUtils.join(viewNameList, ",");
        }
    }

    public boolean isNotNull() {
        return StringUtils.isNotBlank(this.view) && Objects.nonNull(this.selectPath) && this.selectPath.length > 0;
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
        if (StringUtils.isBlank(moduleName)) {
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
        for (TreePath treePath : selectPath) {
            int sortLevel = ONE_LEVEL_SORT;
            //选中的节点 循环是因为可能会存在多选的情况
            if (Objects.nonNull(treePath)) {
                Object[] path = treePath.getPath();
                for (int i = path.length - 1; i >= 0; i--) {
                    Object item = path[i];
                    if (Objects.nonNull(item) && (moduleName.equals(item.toString()) || currProjectName.equals(item.toString()))) {
                        return sortLevel;
                    }
                    sortLevel++;
                }
            }
        }
        //没有匹配到
        return NOT_MATCH;
    }

    @Override
    public String toString() {
        return StringUtils.isNotBlank(this.view) ? this.view : StringUtils.EMPTY;
    }
}
