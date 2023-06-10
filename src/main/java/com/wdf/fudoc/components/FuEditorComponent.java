package com.wdf.fudoc.components;

import com.intellij.ide.IdeBundle;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.TextRange;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ErrorStripeEditorCustomization;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SeparatorFactory;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.factory.LightVirtualFileFactory;
import com.wdf.fudoc.components.listener.FuEditorListener;
import com.wdf.fudoc.util.EditorUtils;
import com.wdf.fudoc.util.FuEditorSettings;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 编辑器组件
 * <p>
 * 分为两部分:1、编辑器内容面板 2、针对编辑器内容的描述面板
 *
 * @author wangdingfu
 * @date 2022-08-17 11:22:33
 */
public class FuEditorComponent {

    /**
     * 主面板
     */
    @Getter
    private JPanel mainPanel;

    /**
     * 描述信息面板
     */
    private JPanel descriptionPanel;
    /**
     * 编辑器显示的内容
     */
    @Getter
    private String content;
    /**
     * 描述信息
     */
    private String description;

    /**
     * 编辑器组件
     */
    @Getter
    private EditorImpl editor;

    /**
     * 是否展示编辑器描述信息面板
     */
    private boolean showDescription;

    /**
     * 指定当前编辑器显示那种类型的语言
     */
    private LightVirtualFile lightVirtualFile;

    private FileType fileType;

    /**
     * 监听器
     */
    private FuEditorListener fuEditorListener;


    public FuEditorComponent(FileType fileType, String content) {
        this(fileType, content, null);
    }

    public FuEditorComponent(FileType fileType, String content, String description) {
        this.content = content;
        setFileType(fileType);
        if (Objects.nonNull(description)) {
            this.description = description;
            this.showDescription = true;
            //初始化描述信息面板
            initDescription();
        }
        //初始化编辑器组件
        initEditor();
        //创建主面板
        createMainPanel();
    }


    /**
     * 创建一个带描述信息的编辑器组件
     *
     * @param content     编辑器显示的内容
     * @param description 描述信息
     * @return 编辑器组件
     */
    public static FuEditorComponent create(String content, FileType fileType, String description) {
        return new FuEditorComponent(fileType, content, description);
    }

    public static FuEditorComponent create(FileType fileType) {
        return new FuEditorComponent(fileType, StringUtils.EMPTY);
    }

    /**
     * 创建一个没有描述信息面板的编辑器组件
     *
     * @param content 编辑器显示的内容
     * @return 编辑器组件
     */
    public static FuEditorComponent create(FileType fileType, String content) {
        return new FuEditorComponent(fileType, content);
    }

    /**
     * 添加编辑器监听器
     *
     * @param fuEditorListener 编辑器监听器
     */
    public void addListener(FuEditorListener fuEditorListener) {
        this.fuEditorListener = fuEditorListener;
    }


    /**
     * 初始化编辑器组件
     */
    private void initEditor() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = EditorUtils.createDocument("", this.fileType);
        this.editor = (EditorImpl) editorFactory.createEditor(document, ProjectUtils.getCurrProject(), EditorKind.UNTYPED);
        //开启右侧的错误条纹
        ErrorStripeEditorCustomization.ENABLED.customize(editor);
        this.refreshUI();
        FuEditorSettings.defaultSetting(this.editor);
        // 添加监控事件
        this.editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(@NotNull DocumentEvent event) {
            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                //修改了编辑器内容
                content = editor.getDocument().getText();
                if (Objects.nonNull(fuEditorListener)) {
                    fuEditorListener.contentChange(content);
                }
            }
        });
    }

    /**
     * 初始化描述信息面板
     */
    private void initDescription() {
        if (this.showDescription) {
            // 描述信息
            JEditorPane editorPane = new JEditorPane();
            // html形式展示
            editorPane.setEditorKit(HTMLEditorKitBuilder.simple());
            // 仅查看
            editorPane.setEditable(false);
            editorPane.setText(this.description);
            // 添加浏览器链接监听事件
            editorPane.addHyperlinkListener(new BrowserHyperlinkListener());

            // 描述面板
            this.descriptionPanel = new JPanel(new GridBagLayout());
            this.descriptionPanel.add(SeparatorFactory.createSeparator(IdeBundle.message("label.description"), null), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, JBUI.insetsBottom(2), 0, 0));
            this.descriptionPanel.add(ScrollPaneFactory.createScrollPane(editorPane), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBUI.insetsTop(2), 0, 0));
        }
    }

    /**
     * 创建主面板
     */
    private void createMainPanel() {
        this.mainPanel = new JPanel(new BorderLayout());
        if (showDescription) {
            // 分割器
            Splitter splitter = new Splitter(true, 0.6F);
            splitter.setFirstComponent(this.editor.getComponent());
            splitter.setSecondComponent(this.descriptionPanel);
            this.mainPanel.add(splitter, BorderLayout.CENTER);
        } else {
            this.mainPanel.add(this.editor.getComponent());
        }
        this.mainPanel.setPreferredSize(JBUI.size(400, 300));
    }

    public void setContent(String content) {
        this.content = content;
        refreshUI();
    }


    private synchronized void refreshUI() {
        Project currProject = ProjectUtils.getCurrProject();
        EditorHighlighterFactory highlighterFactory = EditorHighlighterFactory.getInstance();
        if (StringUtils.isBlank(this.content)) {
            this.editor.setViewer(false);
            // 重置文本内容
            WriteCommandAction.runWriteCommandAction(currProject, () -> this.editor.getDocument().setText(""));
            this.editor.setHighlighter(highlighterFactory.createEditorHighlighter(currProject, lightVirtualFile));
        } else {
            this.content = this.content.replaceAll("\r", "");
            this.editor.setViewer(false);
            // 重置文本内容
            WriteCommandAction.runWriteCommandAction(currProject, () -> this.editor.getDocument().setText(this.content));
            this.editor.setHighlighter(highlighterFactory.createEditorHighlighter(currProject, lightVirtualFile));
        }
    }


    public void append(String appendContent) {
        Project currProject = ProjectUtils.getCurrProject();
        // 获取当前光标所在位置并添加一段文本
        Caret caret = this.editor.getCaretModel().getCurrentCaret();
        DocumentEx document = this.editor.getDocument();
        int offset = caret.getOffset();
        // 在新行前添加和上一行相同的缩进
        int lineNumber = document.getLineNumber(offset);
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        int indentEndOffset = lineStartOffset;
        while (indentEndOffset < offset && Character.isSpaceChar(document.getText(new TextRange(indentEndOffset, indentEndOffset + 1)).charAt(0))) {
            indentEndOffset++;
        }
        String indent = document.getText().substring(lineStartOffset, indentEndOffset);
        String content = appendContent + "\n" + indent;

        int lineRow = StringUtils.countMatches(appendContent, "\n");
        WriteCommandAction.runWriteCommandAction(currProject, () -> this.editor.getDocument().insertString(offset, content));
        VisualPosition visualPosition = caret.getVisualPosition();
        caret.moveToVisualPosition(new VisualPosition(visualPosition.getLine() + lineRow + 1, visualPosition.getColumn()));
    }


    private void setFileType(FileType fileType) {
        this.fileType = fileType;
        this.lightVirtualFile = LightVirtualFileFactory.create(fileType);
    }
}
