package com.wdf.fudoc.request.view.provider;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-12-05 23:07:53
 */
public class FuDocLineMarkerProvider implements LineMarkerProvider {


    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();

        if (element instanceof PsiIdentifier) {

            if (parent instanceof PsiMethod) {
                return createMethodLineMarker(element);
            }
        }
        return null;
    }


    private LineMarkerInfo<PsiElement> createMethodLineMarker(PsiElement element) {

        Project project = element.getProject();
        PsiMethod psiMethod = (PsiMethod) element.getParent();
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);

        if (psiClass == null) {
            return null;
        }
        FuDocContext fuDocContext = new FuDocContext();
        fuDocContext.setSettingData(FuDocSetting.getSettingData());
        fuDocContext.setTargetElement(element);
        return new LineMarkerInfo<>(element, element.getTextRange(), FuDocIcons.FU_DOC, psiElement -> "发起请求", (e, elt) -> {
            httpRequest(psiClass, project, fuDocContext);
        }, GutterIconRenderer.Alignment.LEFT, () -> "Fu Request");
    }


    private void httpRequest(PsiClass psiClass, Project project, FuDocContext fuDocContext) {

        //获取当前接口的唯一标识
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleId = FuDocUtils.getModuleId(module);
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
        String apiKey = FuDocUtils.genApiKey(moduleId, methodId);
        FuHttpRequestData request = FuRequestManager.getRequest(project, apiKey);
        if (Objects.isNull(request)) {
            List<FuDocRootParamData> fuDocRootParamDataList = GenFuDocUtils.genRootParam(fuDocContext, psiClass);
            if (CollectionUtils.isEmpty(fuDocRootParamDataList)) {
                //没有可以请求的方法
                return;
            }
            FuDocRootParamData fuDocRootParamData = fuDocRootParamDataList.get(0);
            //获取当前所属模块
            request = FuHttpRequestDataFactory.build(module, fuDocRootParamData);
        }

        //打开工具窗口
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Fu Request");
        if (toolWindow != null && !toolWindow.isActive()) {
            toolWindow.activate(null);
            Content content = toolWindow.getContentManager().getContent(0);
            if (Objects.nonNull(content)) {
                FuRequestWindow fuRequestWindow = (FuRequestWindow) content.getComponent();
                fuRequestWindow.initData(request);
                toolWindow.getContentManager().setSelectedContent(content);
            }
        }
    }

}
