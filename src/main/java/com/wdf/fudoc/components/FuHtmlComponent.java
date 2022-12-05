package com.wdf.fudoc.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private final Integer width;
    private final Integer height;


    public FuHtmlComponent(@Nullable Project project, String title, String html) {
        this(project, title, html, null, null);
    }

    public FuHtmlComponent(@Nullable Project project, String title, String html, Integer width, Integer height) {
        super(project, true);
        this.html = html;
        this.width = width;
        this.height = height;
        setTitle(title);
        initPanel();
        init();
    }


    @Override
    protected Action @NotNull [] createActions() {
        DialogWrapperAction okAction = new DialogWrapperAction("我已查看") {
            @Override
            protected void doAction(ActionEvent e) {
                close(OK_EXIT_CODE);
            }
        };
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction, new DialogWrapperExitAction("下次再看", CANCEL_EXIT_CODE)};
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
        if (Objects.nonNull(width) && Objects.nonNull(height)) {
            this.jScrollPane.setMinimumSize(new Dimension(width, height));
        }
        return this.jScrollPane;
    }


}
