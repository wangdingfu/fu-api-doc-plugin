package com.wdf.fudoc.components.bo;

import com.google.common.collect.Lists;
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

    @Override
    public String toString() {
        return StringUtils.isNotBlank(this.view) ? this.view : StringUtils.EMPTY;
    }
}
