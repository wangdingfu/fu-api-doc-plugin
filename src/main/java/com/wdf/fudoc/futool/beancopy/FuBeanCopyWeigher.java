package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.wdf.fudoc.futool.beancopy.bo.FuCompletion;

public class FuBeanCopyWeigher extends LookupElementWeigher {
    public FuBeanCopyWeigher() {
        super("FuDocLookupElementWeigher", false, true);
    }

    @Override
    public Integer weigh(LookupElement element) {
        if (element.getObject() instanceof FuCompletion) {
            return Integer.MIN_VALUE;
        }
        return Integer.MAX_VALUE;
    }
}
