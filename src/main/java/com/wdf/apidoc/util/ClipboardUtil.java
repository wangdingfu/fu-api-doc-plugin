package com.wdf.apidoc.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author wangdingfu
 * @descption: 剪贴板工具类
 * @date 2022-05-30 23:26:09
 */
public class ClipboardUtil {


    /**
     * 将数据内容拷贝至剪贴板
     *
     * @param copyContent 需要拷贝到剪贴板的数据内容
     */
    public static void copyToClipboard(String copyContent) {
        StringSelection selection = new StringSelection(copyContent);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
