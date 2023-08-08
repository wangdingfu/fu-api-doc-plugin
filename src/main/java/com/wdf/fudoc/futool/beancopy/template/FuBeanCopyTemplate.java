package com.wdf.fudoc.futool.beancopy.template;

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2023-08-08 13:54:18
 */
public class FuBeanCopyTemplate extends StringBasedPostfixTemplate {


    public FuBeanCopyTemplate() {
        super("beanCopy", "[Fu Doc]", JavaPostfixTemplatesUtils.selectorTopmost(JavaPostfixTemplatesUtils.IS_NON_VOID),null);
    }

    @Override
    public @Nullable String getTemplateString(@NotNull PsiElement element) {
        return "$expr$.beanCopy";
    }
}
