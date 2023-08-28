package com.wdf.fudoc.futool.beancopy.template;

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.futool.beancopy.FuBeanCopyCompletion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.wdf.fudoc.futool.beancopy.FuBeanCopyCompletion.IS_BEAN_COPY;

/**
 * @author wangdingfu
 * @date 2023-08-08 13:54:18
 */
public class FuBeanCopyTemplate extends StringBasedPostfixTemplate {


    public FuBeanCopyTemplate() {
        super(FuBeanCopyCompletion.BEAN_COPY, FuBeanCopyCompletion.TYPE_TEXT, JavaPostfixTemplatesUtils.selectorTopmost(IS_BEAN_COPY), null);
    }


    @Override
    public @Nullable String getTemplateString(@NotNull PsiElement element) {
        return "$expr$." + FuBeanCopyCompletion.BEAN_COPY;
    }
}
