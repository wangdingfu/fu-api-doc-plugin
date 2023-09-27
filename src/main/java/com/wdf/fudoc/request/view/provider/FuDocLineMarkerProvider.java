package com.wdf.fudoc.request.view.provider;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import com.wdf.fudoc.storage.FuDocConfigStorage;
import com.wdf.fudoc.util.FuDocUtils;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-12-05 23:07:53
 */
public class FuDocLineMarkerProvider implements LineMarkerProvider {


    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (element instanceof PsiIdentifier && parent instanceof PsiMethod) {
            return createMethodLineMarker(element);
        }
        return null;
    }


    private LineMarkerInfo<PsiElement> createMethodLineMarker(PsiElement element) {
        Project project = element.getProject();
        PsiMethod psiMethod = (PsiMethod) element.getParent();
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
        if (Objects.isNull(psiClass) || !FuDocUtils.isController(psiClass) || !FuDocUtils.isControllerMethod(psiMethod)) {
            return null;
        }
        if (!FuDocConfigStorage.INSTANCE.readData().isShowControllerIcon()) {
            return null;
        }
        FuDocContext fuDocContext = new FuDocContext();
        fuDocContext.setSettingData(FuDocSetting.getSettingData());
        fuDocContext.setTargetElement(element);
        return new LineMarkerInfo<>(element, element.getTextRange(), FuDocIcons.FU_DOC, psiElement -> "发起请求", (e, elt) -> httpRequest(psiClass, project, fuDocContext), GutterIconRenderer.Alignment.LEFT);
    }


    private void httpRequest(PsiClass psiClass, Project project, FuDocContext fuDocContext) {
        FuHttpRequestData request = FuHttpRequestDataFactory.build(project, psiClass, fuDocContext);
        if (Objects.nonNull(request)) {
            //打开工具窗口
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Fu Request");
            if (toolWindow != null && !toolWindow.isActive()) {
                toolWindow.activate(null);
                Content content = toolWindow.getContentManager().getContent(0);
                if (Objects.nonNull(content)) {
                    FuRequestWindow fuRequestWindow = (FuRequestWindow) content.getComponent();
                    fuRequestWindow.initData(request);
                    fuRequestWindow.setPsiElement(fuDocContext.getTargetElement());
                    toolWindow.getContentManager().setSelectedContent(content);
                }
            }
        }
    }

}
