package com.wdf.fudoc.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * 展示html内容的组件
 *
 * @author wangdingfu
 * @date 2022-11-29 22:30:35
 */
public class FuHtmlComponent extends DialogWrapper {

    private final JEditorPane editorPane = new JEditorPane();
    private JScrollPane jScrollPane;
    private final String html;

    public FuHtmlComponent(@Nullable Project project, String title) {
        this(project, title, "");
    }

    protected FuHtmlComponent(@Nullable Project project, String title, String html) {
        super(project, false);
        this.html = html;
        setTitle(title);
        initPanel();
        init();
    }

    private void initPanel() {
        this.editorPane.setContentType("text/html");
        this.editorPane.setEditable(false);
        this.editorPane.setEditorKit(UIUtil.getHTMLEditorKit());
        this.editorPane.addHyperlinkListener(BrowserHyperlinkListener.INSTANCE);
        UIUtil.doNotScrollToCaret(this.editorPane);
        this.editorPane.setText(this.html);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        if (Objects.isNull(jScrollPane)) {
            this.jScrollPane = ScrollPaneFactory.createScrollPane(editorPane);
        }
        return this.jScrollPane;
    }


}
