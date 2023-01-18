package com.wdf.fudoc.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * @author wangdingfu
 * @date 2023-01-10 13:25:22
 */
public class MarkdownToHtmlUtils {


    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
