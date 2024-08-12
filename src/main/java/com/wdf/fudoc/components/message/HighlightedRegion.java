package com.wdf.fudoc.components.message;

import com.intellij.openapi.editor.markup.TextAttributes;
import cn.fudoc.common.msg.bo.FuMsgItemBO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public final class HighlightedRegion {
  public FuMsgItemBO fuMsgItemBO;
  public TextAttributes textAttributes;


  public HighlightedRegion(FuMsgItemBO fuMsgItemBO, TextAttributes textAttributes) {
    this.fuMsgItemBO = fuMsgItemBO;
    this.textAttributes = textAttributes;
  }
}