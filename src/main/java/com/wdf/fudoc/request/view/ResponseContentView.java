package com.wdf.fudoc.request.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.compat.JsonFileTypeCompat;
import com.wdf.fudoc.util.FuStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

/**
 * 响应内容显示组件
 * 支持多种格式的响应内容显示（JSON、XML、HTML、Text等）
 *
 * @author wangdingfu
 * @date 2023-12-05 11:00:00
 */
@Slf4j
public class ResponseContentView {

    private final Project project;
    private final Disposable parentDisposable;

    private SimpleToolWindowPanel mainPanel;
    private Editor currentEditor;
    private JBLabel fileTypeLabel;
    private JBLabel contentSizeLabel;

    // 缓存当前的文件类型，避免不必要的切换
    private FileType currentFileType;
    private String currentContent;

    // 最大显示内容长度（超过则截断显示）
    private static final int MAX_CONTENT_LENGTH = 10 * 1024 * 1024; // 10MB

    public ResponseContentView(@NotNull Project project, @NotNull Disposable parentDisposable) {
        this.project = project;
        this.parentDisposable = parentDisposable;
        initComponent();

        // 注册到父 Disposable，确保在父对象销毁时释放资源
        Disposer.register(parentDisposable, this::dispose);
    }

    private void initComponent() {
        mainPanel = new SimpleToolWindowPanel(false, true);

        // 创建状态栏（可选：隐藏它）
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(JBUI.Borders.customLineBottom(JBUI.CurrentTheme.ToolWindow.borderColor()));

        fileTypeLabel = new JBLabel("Text");
        contentSizeLabel = new JBLabel("0 bytes");

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        leftPanel.add(fileTypeLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        rightPanel.add(contentSizeLabel);

        statusBar.add(leftPanel, BorderLayout.WEST);
        statusBar.add(rightPanel, BorderLayout.EAST);

        // 注释这行可以隐藏状态栏
        // mainPanel.setToolbar(statusBar);
    }

    /**
     * 设置响应内容
     *
     * @param content 响应内容
     * @param contentType 内容类型
     */
    public void setContent(String content, String contentType) {
        if (content == null) {
            content = "";
        }

        // 更新内容大小
        int byteLength = content.getBytes(StandardCharsets.UTF_8).length;
        String sizeText = formatSize(byteLength);
        contentSizeLabel.setText(sizeText);

        // 根据内容类型选择合适的文件类型
        FileType fileType = determineFileType(content, contentType);
        String fileTypeName = fileType.getName();

        // 检查是否需要更新编辑器（优化性能）
        boolean needUpdateEditor = currentEditor == null ||
                                  !fileType.equals(currentFileType) ||
                                  (content.length() > 1000 && !content.equals(currentContent));

        if (needUpdateEditor) {
            // 如果内容过长，进行截断处理
            String displayContent = content;
            if (content.length() > MAX_CONTENT_LENGTH) {
                displayContent = content.substring(0, MAX_CONTENT_LENGTH) + "\n\n... [内容过长，已截断显示] ...";
            }

            // 更新文件类型标签
            fileTypeLabel.setText(fileTypeName);

            // 创建新的编辑器
            Editor editor = createEditor(displayContent, fileType);

            // 替换当前编辑器
            if (currentEditor != null) {
                EditorFactory.getInstance().releaseEditor(currentEditor);
            }
            currentEditor = editor;

            // 更新面板内容
            mainPanel.setContent(new JBScrollPane(editor.getComponent()));
            mainPanel.revalidate();
            mainPanel.repaint();

            // 缓存当前信息
            currentFileType = fileType;
        } else {
            // 内容较小且文件类型相同，直接更新文本
            if (currentEditor != null && !content.equals(currentContent)) {
                com.intellij.openapi.editor.Document document = currentEditor.getDocument();
                document.setText(content);
            }
        }

        // 缓存当前内容
        currentContent = content;
    }

    /**
     * 创建编辑器
     *
     * @param content 内容
     * @param fileType 文件类型
     * @return 编辑器实例
     */
    private Editor createEditor(String content, FileType fileType) {
        EditorFactory editorFactory = EditorFactory.getInstance();
        Editor editor = editorFactory.createEditor(editorFactory.createDocument(content), project);

        if (editor instanceof EditorEx) {
            EditorEx editorEx = (EditorEx) editor;
            EditorSettings settings = editorEx.getSettings();

            // 设置为只读
            editorEx.setViewer(true);

            // 设置行号显示
            settings.setLineNumbersShown(true);

            // 设置折叠功能
            settings.setFoldingOutlineShown(true);

            // 设置软换行
            settings.setUseSoftWraps(true);

            // 禁用代码高亮优化（对于大文件性能更好）
            settings.setLineMarkerAreaShown(false);
            settings.setIndentGuidesShown(false);
        }

        // 注意：不在这里注册 Disposer，由 setContent 和 dispose 方法统一管理
        // 避免多次调用 setContent 导致的多次注册问题

        return editor;
    }

    /**
     * 根据内容和内容类型确定文件类型
     *
     * @param content 内容
     * @param contentType HTTP响应头中的Content-Type
     * @return 文件类型
     */
    private FileType determineFileType(String content, String contentType) {
        // 首先根据Content-Type头判断
        if (FuStringUtils.isNotBlank(contentType)) {
            String lowerContentType = contentType.toLowerCase();

            if (lowerContentType.contains("application/json")) {
                return JsonFileTypeCompat.getJsonFileType();
            } else if (lowerContentType.contains("text/xml") || lowerContentType.contains("application/xml")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("xml");
            } else if (lowerContentType.contains("text/html")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("html");
            } else if (lowerContentType.contains("application/javascript") || lowerContentType.contains("text/javascript")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("js");
            } else if (lowerContentType.contains("text/css")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("css");
            } else if (lowerContentType.contains("application/yaml") || lowerContentType.contains("text/yaml")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("yaml");
            }
        }

        // 根据内容判断
        if (FuStringUtils.isNotBlank(content)) {
            String trimmedContent = content.trim();

            // JSON格式判断
            if ((trimmedContent.startsWith("{") && trimmedContent.endsWith("}")) ||
                (trimmedContent.startsWith("[") && trimmedContent.endsWith("]"))) {
                try {
                    // 尝试解析JSON
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    mapper.readTree(trimmedContent);
                    return JsonFileTypeCompat.getJsonFileType();
                } catch (Exception e) {
                    // 不是JSON，继续判断其他格式
                }
            }

            // XML格式判断
            if (trimmedContent.startsWith("<?xml") || trimmedContent.startsWith("<")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("xml");
            }

            // HTML格式判断
            if (trimmedContent.startsWith("<!DOCTYPE html") ||
                trimmedContent.startsWith("<html") ||
                trimmedContent.toLowerCase().contains("<div")) {
                return FileTypeManager.getInstance().getFileTypeByExtension("html");
            }
        }

        // 默认返回纯文本类型
        return FileTypeManager.getInstance().getFileTypeByExtension("txt");
    }

    /**
     * 格式化文件大小显示
     *
     * @param bytes 字节数
     * @return 格式化后的字符串
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 获取主面板
     *
     * @return 主面板
     */
    public JComponent getComponent() {
        return mainPanel;
    }

    /**
     * 释放资源
     */
    public void dispose() {
        if (currentEditor != null && !currentEditor.isDisposed()) {
            EditorFactory.getInstance().releaseEditor(currentEditor);
            currentEditor = null;
        }
        // 清理缓存
        currentFileType = null;
        currentContent = null;
    }
}