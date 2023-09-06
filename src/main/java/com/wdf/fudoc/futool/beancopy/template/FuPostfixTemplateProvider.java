package com.wdf.fudoc.futool.beancopy.template;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author wangdingfu
 * @date 2023-08-08 11:06:19
 */
public class FuPostfixTemplateProvider implements PostfixTemplateProvider {
    @Override
    public @NotNull Set<PostfixTemplate> getTemplates() {
        return Sets.newHashSet(new FuBeanCopyTemplate());
    }

    @Override
    public boolean isTerminalSymbol(char currentChar) {
        return currentChar == '.';
    }

    @Override
    public void preExpand(@NotNull PsiFile file, @NotNull Editor editor) {

    }

    @Override
    public void afterExpand(@NotNull PsiFile file, @NotNull Editor editor) {

    }

    @Override
    public @NotNull PsiFile preCheck(@NotNull PsiFile copyFile, @NotNull Editor realEditor, int currentOffset) {
        return copyFile;
    }
}
