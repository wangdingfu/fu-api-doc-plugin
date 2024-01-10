package com.wdf.fudoc.components.message;

import com.google.common.collect.Lists;
import com.intellij.ide.ui.AntialiasingType;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.ui.paint.LinePainter2D;
import com.intellij.util.ui.GraphicsUtil;
import com.wdf.api.msg.bo.FuMsgItemBO;
import com.wdf.api.enumtype.MessageType;
import com.wdf.fudoc.util.ColorUtils;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;
import org.jetbrains.annotations.Nls;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * 高亮文本组件
 *
 * @author wangdingfu
 * @date 2022-12-03 13:29:36
 */
public class FuHighlightComponent extends JComponent implements Accessible {


    /**
     * 高亮文本集合
     */
    protected List<HighlightedRegion> highlightedRegionList;

    @Setter
    protected String myText;


    @Override
    public void updateUI() {
        GraphicsUtil.setAntialiasingType(this, AntialiasingType.getAAHintForSwingComponent());
    }

    /**
     * 当前是否点击了消息【特殊消息】
     */
    protected boolean isMousePressed() {
        return false;
    }

    /**
     * 当前鼠标是否经过消息【特殊消息】
     */
    protected boolean isMouseHover() {
        return false;
    }

    /**
     * 获取当前鼠标选中的消息【特殊消息】
     */
    protected String getCurrentMsgId() {
        return null;
    }

    /**
     * 清空上一条消息
     */
    public void clear() {
        if (CollectionUtils.isNotEmpty(highlightedRegionList)) {
            highlightedRegionList.clear();
        }
    }

    /**
     * 添加高亮文本
     *
     * @param fuMsgItemBO 消息对象
     * @param attributes  消息属性
     */
    public void addHighlighter(FuMsgItemBO fuMsgItemBO, TextAttributes attributes) {
        if (CollectionUtils.isEmpty(highlightedRegionList)) {
            highlightedRegionList = Lists.newArrayList();
        }
        highlightedRegionList.add(new HighlightedRegion(fuMsgItemBO, attributes));
    }


    /**
     * 绘制组件
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (CollectionUtils.isNotEmpty(highlightedRegionList)) {
            Color fgColor = JBColor.DARK_GRAY;
            //初始化文本x轴坐标
            int offset = getXOffset();
            applyRenderingHints(g);
            FontMetrics defFontMetrics = getFontMetrics(getFont());
            //获取文本y轴的坐标
            final int yOffset = (getHeight() - defFontMetrics.getMaxAscent() - defFontMetrics.getMaxDescent()) / 2 + defFontMetrics.getMaxAscent();
            //循环绘制消息的每一段
            for (HighlightedRegion highlightedRegion : highlightedRegionList) {
                FuMsgItemBO fuMsgItemBO = highlightedRegion.fuMsgItemBO;
                TextAttributes textAttributes = highlightedRegion.getTextAttributes();
                String text = fuMsgItemBO.getText();
                if (MessageType.NORMAL.getCode().equals(fuMsgItemBO.getMsgType()) || Objects.isNull(textAttributes)) {
                    //绘画普通文本
                    JBColor color = ColorUtils.getColor(fuMsgItemBO);
                    Color fontColor = Objects.isNull(color) ? fgColor : color;
                    g.setColor(fontColor);
                    g.setFont(defFontMetrics.getFont());
                } else {
                    String currentMsgId = getCurrentMsgId();
                    //绘画特殊文本（有点击效果）
                    Font regFont = getFont().deriveFont(textAttributes.getFontType());
                    FontMetrics fontMetrics = getFontMetrics(regFont);
                    //获取当前文本需要展示的颜色
                    Color color = isMousePressed() ? textAttributes.getForegroundColor() : textAttributes.getBackgroundColor();
                    g.setColor(color);
                    g.setFont(defFontMetrics.getFont());
                    if (isMouseHover() && fuMsgItemBO.getMsgId().equals(currentMsgId)) {
                        //当鼠标进过时 为经过的特殊消息画上下划线
                        g.setColor(textAttributes.getEffectColor());
                        int y = yOffset + 2;
                        LinePainter2D.paint((Graphics2D) g, offset + 1, y, offset + fontMetrics.stringWidth(text) - 1, y);
                    }
                }
                //绘制文本
                g.drawString(text, offset, yOffset);
                offset += defFontMetrics.stringWidth(text);
            }
        }
        super.paintComponent(g);
    }


    /**
     * 获取当前光标位于那一段消息上
     *
     * @param x 光标所在位置
     * @return 光标位置上的消息
     */
    public HighlightedRegion findRegionByX(int x) {
        FontMetrics defFontMetrics = getFontMetrics(getFont());
        int width = 4;
        if (width > x) return null;

        if (CollectionUtils.isNotEmpty(highlightedRegionList)) {
            for (HighlightedRegion highlightedRegion : highlightedRegionList) {
                FuMsgItemBO fuMsgItemBO = highlightedRegion.getFuMsgItemBO();
                width += defFontMetrics.stringWidth(fuMsgItemBO.getText());
                if (width >= x) return highlightedRegion;
            }
        }
        return null;
    }


    /**
     * 获取初始化x轴位置
     */
    protected int getXOffset() {
        return 4;
    }


    protected void applyRenderingHints(Graphics g) {
        UISettings.setupAntialiasing(g);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics defFontMetrics = getFontMetrics(getFont());

        int height = defFontMetrics.getHeight() + defFontMetrics.getLeading();

        return new Dimension(getStringWidth(myText, defFontMetrics) + 10, height);
    }

    protected int getStringWidth(@Nls String text, FontMetrics fontMetrics) {
        if (FuStringUtils.isBlank(text)) {
            return 0;
        }
        return fontMetrics.stringWidth(text);
    }
}
