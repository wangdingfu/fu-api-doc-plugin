package com.wdf.fudoc.navigation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
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

    private final RequestType requestType;

    private final String location;

    private final String title;

    public ApiNavigationItem(PsiMethod psiMethod, String url, RequestType requestType, String location, String title) {
        this.psiElement = psiMethod;
        this.url = url;
        this.requestType = requestType;
        this.location = location;
        this.title = title;
    }


    /**
     * api搜索结果列表中 url后面跟着的描述文本
     */
    @NotNull
    public String getRightText() {
        if (StringUtils.isNotBlank(title)) {
            return title + "   " + location;
        }
        return location;
    }
}
