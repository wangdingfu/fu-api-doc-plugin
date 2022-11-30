package com.wdf.fudoc.components.test;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class HighlightedRegion {
  public FuMsgItemBO fuMsgItemBO;
  public int startOffset;
  public int endOffset;
  public TextAttributes textAttributes;

}