package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.idea.maven.model.MavenArtifact;
import org.jetbrains.idea.maven.model.MavenExplicitProfiles;
import org.jetbrains.idea.maven.model.MavenProfile;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectReader;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @date 2023-07-03 19:00:54
 */
public class MavenUtils {


    public static List<String> getActiveProfiles(Module module) {
        Project project = module.getProject();

        // 获取 Maven 项目管理器
        MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(project);
        MavenProject mavenProject = mavenProjectsManager.findProject(module);
        if (Objects.isNull(mavenProject)) {
            return Lists.newArrayList();
        }
        MavenExplicitProfiles activatedProfilesIds = mavenProject.getActivatedProfilesIds();
        Collection<String> enabledProfiles = activatedProfilesIds.getEnabledProfiles();
        return Lists.newArrayList(enabledProfiles);
    }


    public static List<Module> getChildModule(Module module, Collection<Module> allModules) {
        if (Objects.isNull(module) || CollectionUtils.isNotEmpty(allModules)) {
            return Lists.newArrayList();
        }
        MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(module.getProject());
        MavenProject mavenProject = mavenProjectsManager.findProject(module);
        if (Objects.isNull(mavenProject)) {
            return Lists.newArrayList();
        }
        List<Module> childList = Lists.newArrayList();
        for (Module moduleInfo : allModules) {
            MavenProject project = mavenProjectsManager.findProject(module);
            if (Objects.nonNull(project) && Objects.nonNull(mavenProject.findDependencies(project.getMavenId()))) {
                childList.add(moduleInfo);
            }
        }
        return childList;
    }

}
