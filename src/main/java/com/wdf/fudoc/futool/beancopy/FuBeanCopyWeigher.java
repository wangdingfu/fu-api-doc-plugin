package com.wdf.fudoc.futool.beancopy;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.wdf.fudoc.test.action.FuCompletion;

public class FuBeanCopyWeigher extends LookupElementWeigher {
  public FuBeanCopyWeigher() {
    super("FuDocLookupElementWeigher", false, true);
  }

  @Override
  public Integer weigh(LookupElement element) {
    if (element.getObject() instanceof FuCompletion) {
      return ((FuCompletion) element.getObject()).getIndex();
    }
    return Integer.MAX_VALUE;
  }
}
