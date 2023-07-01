package com.wdf.fudoc.request.po;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 全局请求头｜全局变量
 *
 * @author wangdingfu
 * @date 2023-06-10 21:54:18
 */
@Getter
@Setter
public class GlobalKeyValuePO extends HeaderKeyValueBO {

    public GlobalKeyValuePO() {
        super.setLevel("全局请求头");
    }

    /**
     * 作用范围
     */
    private TreePathBO scope;


    public boolean isScope(Module module) {
        List<String> selectPathList;
        if (Objects.isNull(module) || Objects.isNull(scope) || CollectionUtils.isEmpty(selectPathList = scope.getSelectPathList())) {
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

}
