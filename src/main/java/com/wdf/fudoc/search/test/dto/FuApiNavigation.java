package com.wdf.fudoc.search.test.dto;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.PsiNavigateUtil;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-05-24 11:50:14
 */
@Getter
@Slf4j
public class FuApiNavigation implements NavigationItem {

    private PsiElement psiElement;
    private PsiMethod psiMethod;

    private RequestType requestType;
    private String url;
    private MethodPathInfo methodPathInfo;

    public FuApiNavigation(PsiElement psiElement, RequestType requestType, String url, MethodPathInfo methodPathInfo) {
        this.requestType = requestType;
        this.url = url;
        this.methodPathInfo = methodPathInfo;
        this.psiElement = psiElement;
        if (psiElement instanceof PsiMethod) {
            this.psiMethod = (PsiMethod) psiElement;
        }
    }

    @NotNull
    public String getRightText() {
        if (StringUtils.isNotBlank(methodPathInfo.getMethodDesc())) {
            return methodPathInfo.getMethodDesc() + " " + methodPathInfo.getLocation();
        } else {
            return methodPathInfo.getLocation();
        }
    }

    @Override
    public String toString() {
        return getRightText();
    }

    @Override
    public @Nullable @NlsSafe String getName() {
        return this.url;
    }

    @Override
    public @Nullable ItemPresentation getPresentation() {
        return new ItemPresentation() {

            @Nullable
            @Override
            public String getPresentableText() {
                return getUrl();
            }

            @Override
            public String getLocationString() {
                String location = null;

                if (psiElement instanceof PsiMethod) {
                    PsiMethod psiMethod = ((PsiMethod) psiElement);
                    PsiClass psiClass = psiMethod.getContainingClass();
                    if (psiClass != null) {
                        location = psiClass.getName();
                    }
                    location += "#" + psiMethod.getName();
                    location = "Java: (" + location + ")";
                }

                if (psiElement != null) {
                    location += " in " + psiElement.getResolveScope().getDisplayName();
                }
                log.info("getLocationString：{}",location);
                return location;
            }

            @NotNull
            @Override
            public Icon getIcon(boolean unused) {
                return requestType.getIcon();
            }
        };
    }

    @Override
    public void navigate(boolean requestFocus) {
        PsiNavigateUtil.navigate(this.psiElement);

    }

    @Override
    public boolean canNavigate() {
        if (psiElement instanceof Navigatable) {
            return ((Navigatable) psiElement).canNavigate();
        }
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }
}
