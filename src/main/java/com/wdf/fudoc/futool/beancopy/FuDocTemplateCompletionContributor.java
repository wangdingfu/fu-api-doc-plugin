package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;

/**
 * @author wangdingfu
 * @date 2023-08-08 18:46:21
 */
public class FuDocTemplateCompletionContributor extends CompletionContributor {

    public FuDocTemplateCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new FuDocTemplatesCompletionProvider());
    }
}
