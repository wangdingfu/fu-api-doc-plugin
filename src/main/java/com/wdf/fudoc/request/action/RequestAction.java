package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.api.base.FuBundle;
import com.wdf.api.notification.FuDocNotification;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 弹出http请求窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:01:04
 */
public class RequestAction extends AbstractRequestAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        FuHttpRequestData requestData = getRequestData(e, psiClass, fuDocContext);
        if (Objects.isNull(requestData)) {
            FuDocNotification.notifyError(FuBundle.message("fudoc.request.api.notFund"));
            return;
        }
        HttpDialogView httpDialogView = null;
        Project project = e.getProject();
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        if (ViewMode.SINGLE_PINNED.myActionID.equals(fuRequestConfigPO.getViewMode())) {
            //指定了只展示单个窗体 将当前激活的窗体都手动给关闭
            httpDialogView = FuRequestManager.closeAll(project);
        }

        if (Objects.isNull(httpDialogView)) {
            httpDialogView = new HttpDialogView(e.getProject(), fuDocContext.getTargetElement(), requestData);
            httpDialogView.show();
        } else {
            //重置请求信息
            httpDialogView.saveData();
            httpDialogView.reset(fuDocContext.getTargetElement(), requestData);
        }
        FuRequestManager.add(httpDialogView);
    }


}
