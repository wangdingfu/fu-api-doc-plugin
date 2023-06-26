package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.AbstractClassAction;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import com.wdf.fudoc.util.FuDocUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 弹出http请求窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:01:04
 */
public class RequestAction extends AbstractClassAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        FuHttpRequestData request;
        if (!JavaClassType.CONTROLLER.equals(JavaClassType.get(psiClass))
                || Objects.isNull(request = FuHttpRequestDataFactory.build(e.getProject(), psiClass, fuDocContext))) {
            //获取最近一次请求记录
            Module module = ModuleUtil.findModuleForPsiElement(fuDocContext.getTargetElement());
            request = FuRequestManager.getRecent(e.getProject(), FuDocUtils.getModuleId(module));
            if (Objects.isNull(request)) {
                return;
            }
            request.setModule(module);
        }
        HttpDialogView httpDialogView = null;
        Project project = e.getProject();
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorageFactory.get(e.getProject()).readData();
        if (ViewMode.SINGLE_PINNED.myActionID.equals(fuRequestConfigPO.getViewMode())) {
            //指定了只展示单个窗体 将当前激活的窗体都手动给关闭
            httpDialogView = FuRequestManager.closeAll(project);
        }
        if (Objects.isNull(httpDialogView)) {
            httpDialogView = new HttpDialogView(e.getProject(), fuDocContext.getTargetElement(), request);
            httpDialogView.show();
        } else {
            httpDialogView.reset(fuDocContext.getTargetElement(), request);
        }
        FuRequestManager.add(httpDialogView);
    }
}
