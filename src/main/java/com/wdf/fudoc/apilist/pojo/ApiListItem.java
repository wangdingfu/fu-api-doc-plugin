package com.wdf.fudoc.apilist.pojo;

import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * API 列表项数据对象
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
@Setter
public class ApiListItem {

    /**
     * 对应的 PsiMethod
     */
    private final PsiMethod psiMethod;

    /**
     * API 请求 URL
     */
    private final String url;

    /**
     * 请求类型 (GET/POST/PUT/DELETE etc.)
     */
    private final RequestType requestType;

    /**
     * API 标题/描述 (从注释中提取)
     */
    private final String title;

    /**
     * 所属 Module 名称
     */
    private final String moduleName;

    /**
     * Controller 类的全限定名
     */
    private final String className;

    /**
     * URL 路径前缀 (第一级路径, 如 /api, /user)
     */
    private String urlPrefix;

    public ApiListItem(@NotNull PsiMethod psiMethod,
                       @NotNull String url,
                       @NotNull RequestType requestType,
                       @Nullable String title,
                       @Nullable String moduleName,
                       @NotNull String className) {
        this.psiMethod = psiMethod;
        this.url = url;
        this.requestType = requestType;
        this.title = title;
        this.moduleName = moduleName;
        this.className = className;
        this.urlPrefix = extractUrlPrefix(url);
    }

    /**
     * 提取 URL 的第一级路径作为前缀
     * 例如: /api/user/list -> /api
     *      /user/info -> /user
     *      / -> /
     */
    private String extractUrlPrefix(String url) {
        if (FuStringUtils.isBlank(url) || "/".equals(url)) {
            return "/";
        }
        // 移除开头的 /
        String path = url.startsWith("/") ? url.substring(1) : url;
        // 找到第一个 / 的位置
        int firstSlash = path.indexOf('/');
        if (firstSlash > 0) {
            return "/" + path.substring(0, firstSlash);
        }
        // 如果没有第二个 /, 说明只有一级路径
        return "/" + path;
    }

    /**
     * 获取显示文本
     */
    @NotNull
    public String getDisplayText() {
        if (FuStringUtils.isNotBlank(title)) {
            return title;
        }
        return url;
    }

    /**
     * 获取方法签名 (类名.方法名)
     */
    @NotNull
    public String getMethodSignature() {
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        return simpleClassName + "." + psiMethod.getName() + "()";
    }
}
