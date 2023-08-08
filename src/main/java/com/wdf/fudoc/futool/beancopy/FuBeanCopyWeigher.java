package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.codeInsight.template.impl.LiveTemplateLookupElement;
import com.intellij.openapi.util.registry.Registry;
import com.wdf.fudoc.futool.beancopy.bo.FuCompletion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FuBeanCopyWeigher extends LookupElementWeigher {
    public FuBeanCopyWeigher() {
        super("templates", Registry.is("ide.completion.show.live.templates.on.top"), false);
    }

    @Nullable
    @Override
    public Comparable weigh(@NotNull LookupElement element) {
        return element.getObject() instanceof FuCompletion;
    }
}
