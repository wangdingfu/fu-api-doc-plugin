package com.wdf.fudoc.components.tree;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-07 19:41:08
 */
public class FuModuleTreeStructure extends SimpleTreeStructure {

    /**
     * 根module
     */
    private final ModuleNode root;

    public FuModuleTreeStructure(Project project) {
        String projectName = project.getName();
        this.root = new ModuleNode(projectName, null);
        Module[] modules = ModuleManager.getInstance(project).getModules();
        if (modules != null && modules.length > 0) {
            List<ModuleNode> children = root.getChildList();
            for (Module module : modules) {
                String name = module.getName();
                if (projectName.equals(name)) {
                    continue;
                }
                children.add(new ModuleNode(module.getName(), root));
            }
        }
    }


    @Override
    public @NotNull Object getRootElement() {
        return root;
    }

    public ModuleNode getRoot() {
        return root;
    }
}
