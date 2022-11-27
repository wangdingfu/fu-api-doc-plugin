package com.wdf.fudoc.components.test;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
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

    public void appendText(String msgId, @Nls String text, TextAttributes attributes) {
        int startOffset = myBuffer.length();
        myBuffer.append(text);
        if (attributes != null) {
            myHighlightedRegions.add(new HighlightedRegion(msgId, startOffset, myBuffer.length(), attributes));
        }
    }

    public void appendText(String msgId, char[] text, TextAttributes attributes) {
        int startOffset = myBuffer.length();
        myBuffer.append(text);
        if (attributes != null) {
            myHighlightedRegions.add(new HighlightedRegion(msgId, startOffset, myBuffer.length(), attributes));
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
            renderer.addHighlighter(info.getMsgId(), info.startOffset, info.endOffset, info.textAttributes);
        }
    }
}