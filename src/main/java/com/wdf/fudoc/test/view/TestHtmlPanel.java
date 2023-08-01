package com.wdf.fudoc.test.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-11-28 22:27:25
 */
public class TestHtmlPanel extends DialogWrapper{
    static String html = "<html>\n" +
            "<head>\n" +
            "    <style type=\"text/css\">\n" +
            "        body {\n" +
            "            margin: 10px;\n" +
            "            background: #C0C0C0;\n" +
            "            color: #333333;\n" +
            "        }\n" +
            "\n" +
            "        code {\n" +
            "            background-color: #eeee11;\n" +
            "            margin: 4px;\n" +
            "        }\n" +
            "\n" +
            "        pre {\n" +
            "            padding: 10px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>This is HTML rendering demo.</h1>\n" +
            "Some key features are supported:\n" +
            "<ol>\n" +
            "    <li>Of course <i>Italic</i> & <b>bold</b></li>\n" +
            "    <li>Not so often used <sub>subscript</sub> <strike>strikethrough</strike> and <sup>superscript</sup>\n" +
            "    <li>Tags <small>small</small> and <strong>strong</strong>. And <u>underlined</u> too.</li>\n" +
            "    <li><a href=\"https://www.jetbrains.com/\">External links</a></li>\n" +
            "    <li>This is <code>@Code</code> tag to be <code>highlighted</code></li>\n" +
            "    <li>Emoji etc. if you are lucky enough to see it <span style=\"color:red;\">[&#9829;]</span>[&#128512;]</li>\n" +
            "</ol>\n" +
            "The tag below is &lt;pre&gt;:\n" +
            "<pre style=\"background-color:white;color:black;\">\n" +
            "class HelloWorld {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello World!\");\n" +
            "    }\n" +
            "}</pre>\n" +
            "<ul>\n" +
            "    <li>User-friendly link <a href=\"https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/text/html/CSS.html\">\n" +
            "        CSS support in Java engine</a></li>\n" +
            "    <li>Not so user-friendly link <a href=\"https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/text/html/CSS.html\">\n" +
            "        https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/text/html/CSS.html</a></li>\n" +
            "</ul>\n" +
            "<p>\n" +
            "<div style=\"color:white;background-color:#909090; border:solid 1px #fedcba; text-align:center;\">Outer div\n" +
            "    <div style=\"background-color:#606060; width:50%; margin:20px;padding:11px; border:solid 1px orange;\">Inner div with width 50%</div>\n" +
            "</div>\n" +
            "</p><br>\n" +
            "<p>\n" +
            "    Well, we can do something with it...</p>\n" +
            "</body>\n" +
            "</html>";

    public TestHtmlPanel(@Nullable Project project) {
        super(project);
        setTitle("测试html消息通知");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return get();
    }

    public static JScrollPane get() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
//        editorPane.setEditorKit(HTMLEditorKitBuilder.simple());
        editorPane.addHyperlinkListener(BrowserHyperlinkListener.INSTANCE);
        UIUtil.doNotScrollToCaret(editorPane);
        editorPane.setText(html);
        return ScrollPaneFactory.createScrollPane(editorPane);
    }

}
