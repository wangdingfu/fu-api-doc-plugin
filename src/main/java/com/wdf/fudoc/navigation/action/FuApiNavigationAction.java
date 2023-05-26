package com.wdf.fudoc.navigation.action;

import com.intellij.ide.actions.SearchEverywhereBaseAction;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.navigation.FuApiNavigationContributor;
import org.jetbrains.annotations.NotNull;

/**
 * 搜索API入口
 *
 * @author wangdingfu
 * @date 2023-05-24 11:28:31
 */
public class FuApiNavigationAction extends SearchEverywhereBaseAction implements DumbAware {

    public FuApiNavigationAction() {
        Presentation presentation = getTemplatePresentation();
        presentation.setText("Search Api");
        presentation.setDescription(FuDocMessageBundle.message(MessageConstants.SEARCH_API_DESCRIPTION));
        addTextOverride(ActionPlaces.MAIN_MENU, "FuApi");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        boolean dumb = DumbService.isDumb(project);
        if (!dumb) {
            showInSearchEverywherePopup(FuApiNavigationContributor.INSTANCE, e, true, true);
        }
    }
}
