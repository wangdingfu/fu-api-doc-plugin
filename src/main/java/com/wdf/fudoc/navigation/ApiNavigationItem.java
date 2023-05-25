package com.wdf.fudoc.navigation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.navigation.dto.MethodPathInfo;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 导航API的数据对象
 *
 * @author wangdingfu
 * @date 2023-05-25 18:49:26
 */
@Getter
public class ApiNavigationItem {

    private final PsiElement psiElement;

    private final String url;

    private PsiMethod psiMethod;

    private final RequestType requestType;
    private final MethodPathInfo methodPathInfo;

    public ApiNavigationItem(PsiElement psiElement, RequestType requestType, String url, MethodPathInfo methodPathInfo) {
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
}
