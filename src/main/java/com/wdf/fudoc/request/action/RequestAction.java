package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.AbstractClassAction;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpDialogView;
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
            request = FuRequestManager.getRecent(e.getProject(), FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(psiClass)));
            if (Objects.isNull(request)) {
                return;
            }
        }
        HttpDialogView.popup(e.getProject(), fuDocContext.getTargetElement(), request);
    }
}
