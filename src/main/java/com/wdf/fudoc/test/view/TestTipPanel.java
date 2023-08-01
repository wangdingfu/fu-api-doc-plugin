package com.wdf.fudoc.test.view;

import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.UIUtil;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2022-11-26 20:23:29
 */
public class TestTipPanel {

    @Getter
    private JPanel rootPanel;

    private JEditorPane editorPane;

    public TestTipPanel() {
        this.rootPanel = new JPanel(new BorderLayout());
        JLabel testLabel = new LinkLabel<>("【测试链接label】", null, (aSource, aLinkData) -> {
            System.out.println(aSource);
        });
        HyperlinkLabel readerModeHyperLink = createReaderModeHyperLink();
        this.rootPanel.add(testLabel,BorderLayout.WEST);
        this.rootPanel.add(readerModeHyperLink,BorderLayout.SOUTH);

    }


    private static HyperlinkLabel createReaderModeHyperLink() {
        HyperlinkLabel hyperlinkLabel = new HyperlinkLabel();
        hyperlinkLabel.setTextWithHyperlink("这是一条测试消息 <hyperlink>给我点赞</hyperlink>");
        hyperlinkLabel.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, hyperlinkLabel);

        hyperlinkLabel.addHyperlinkListener(e -> {
            System.out.println("123");
        });
        return hyperlinkLabel;
    }


}
