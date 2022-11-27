package com.wdf.fudoc.components.test;

import com.intellij.openapi.editor.markup.TextAttributes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class HighlightedRegion {
  public String msgId;
  public int startOffset;
  public int endOffset;
  public TextAttributes textAttributes;

}