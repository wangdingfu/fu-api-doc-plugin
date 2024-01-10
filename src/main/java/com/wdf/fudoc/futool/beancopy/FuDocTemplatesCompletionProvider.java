package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import com.wdf.api.enumtype.FuColor;
import com.wdf.fudoc.futool.beancopy.bo.FuCompletion;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2023-08-08 18:46:53
 */
public class FuDocTemplatesCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final String TYPE_TEXT = "[Fu Doc]";
    private static final String BEAN_COPY = "beanCopy";
    private static final String BEAN_COPY_CN = " 拷贝对象";

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        result.addElement(buildBeanCopy());
    }

    private LookupElementBuilder buildBeanCopy() {
        return LookupElementBuilder.create(new FuCompletion(), BEAN_COPY).withIcon(FuDocIcons.FU_DOC)
                .withPresentableText(BEAN_COPY)
                .withItemTextForeground(FuColor.GREEN.color())
                .appendTailText(BEAN_COPY_CN, true)
                .withTypeText(TYPE_TEXT).bold();
    }
}
