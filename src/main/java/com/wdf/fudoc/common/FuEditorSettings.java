package com.wdf.fudoc.common;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;

/**
 * @author wangdingfu
 * @date 2022-08-17 11:37:22
 */
public class FuEditorSettings {

    /**
     * 初始化编辑器组件的一些默认设置
     *
     * @param editor 编辑器组件
     */
    public static void defaultSetting(Editor editor) {
        EditorSettings editorSettings = editor.getSettings();
        // 关闭虚拟空间
        editorSettings.setVirtualSpace(false);
        // 关闭标记位置（断点位置）
        editorSettings.setLineMarkerAreaShown(false);
        // 关闭缩减指南
        editorSettings.setIndentGuidesShown(false);
        // 显示行号
        editorSettings.setLineNumbersShown(false);
        // 支持代码折叠
        editorSettings.setFoldingOutlineShown(true);
        // 附加行，附加列（提高视野）
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        // 不显示换行符号
        editorSettings.setCaretRowShown(false);
    }
}
