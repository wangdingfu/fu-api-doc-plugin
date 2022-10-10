package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.common.AbstractClassAction;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 弹出http请求窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:01:04
 */
public class RequestAction extends AbstractClassAction {

    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.CONTROLLER.equals(javaClassType);
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        //获取当前接口的唯一标识
        String moduleId = FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(psiClass));
        //获取当前操作的方法
        PsiMethod targetMethod = PsiClassUtils.getTargetMethod(fuDocContext.getTargetElement());
        if (Objects.isNull(targetMethod)) {
            PsiMethod[] methods = psiClass.getMethods();
            if (methods.length <= 0) {
                return;
            }
            targetMethod = methods[0];
        }
        String methodId = PsiClassUtils.getMethodId(targetMethod);
        //当前接口的唯一标识
        String apiKey = moduleId + ":" + methodId;
        FuHttpRequestData request = FuRequestManager.getRequest(e.getProject(), apiKey);
        if (Objects.isNull(request)) {
            List<FuDocRootParamData> fuDocRootParamDataList = GenFuDocUtils.genRootParam(fuDocContext, psiClass);
            if (CollectionUtils.isEmpty(fuDocRootParamDataList)) {
                //没有可以请求的方法
                return;
            }
            FuDocRootParamData fuDocRootParamData = fuDocRootParamDataList.get(0);
            //获取当前所属模块
            request = FuHttpRequestDataFactory.build(moduleId, fuDocRootParamData);
        }
        HttpDialogView.popup(e.getProject(), request);
    }
}
