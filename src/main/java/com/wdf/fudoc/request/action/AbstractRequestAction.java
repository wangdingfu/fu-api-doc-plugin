package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.AbstractClassAction;
import com.wdf.fudoc.common.enumtype.FuDocAction;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.util.FuDocUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-27 21:13:22
 */
public abstract class AbstractRequestAction extends AbstractClassAction {


    @Override
    protected FuDocAction getAction() {
        return null;
    }

    public AbstractRequestAction() {
    }

    public AbstractRequestAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    protected FuHttpRequestData getRequestData(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        FuHttpRequestData request = null;
        if (JavaClassType.CONTROLLER.equals(JavaClassType.get(psiClass))) {
            //controller中发起 构建光标所在接口信息
            request = FuHttpRequestDataFactory.build(e.getProject(), psiClass, fuDocContext);
        }
        if (Objects.isNull(request)) {
            //获取最近一次请求记录
            Module module = ModuleUtil.findModuleForPsiElement(fuDocContext.getTargetElement());
            request = FuRequestManager.getRecent(e.getProject(), FuDocUtils.getModuleId(module));
            if (Objects.nonNull(request)) {
                request.setModule(module);
            }
        }
        return request;
    }
}
