package com.wdf.fudoc.components.test;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class HighlightedText {
    private final @Nls StringBuilder myBuffer;
    private final List<HighlightedRegion> myHighlightedRegions = new ArrayList<>();

    public HighlightedText() {
        myBuffer = new StringBuilder();
    }

    public void appendText(FuMsgItemBO fuMsgItemBO, TextAttributes attributes) {
        int startOffset = myBuffer.length();
        myBuffer.append(fuMsgItemBO.getText());
        if (attributes != null) {
            myHighlightedRegions.add(new HighlightedRegion(fuMsgItemBO, startOffset, myBuffer.length(), attributes));
        }
    }


    public boolean equals(Object o) {
        if (!(o instanceof HighlightedText)) return false;

        HighlightedText highlightedText = (HighlightedText) o;

        return StringUtil.equals(myBuffer, highlightedText.myBuffer) && myHighlightedRegions.equals(highlightedText.myHighlightedRegions);
    }

    @NotNull
    @Nls
    public String getText() {
        return myBuffer.toString();
    }

    public void applyToComponent(HighlightableComponent renderer) {
        renderer.setText(myBuffer.toString());
        for (HighlightedRegion info : myHighlightedRegions) {
            renderer.addHighlighter(info.getFuMsgItemBO(), info.startOffset, info.endOffset, info.textAttributes);
        }
    }
}