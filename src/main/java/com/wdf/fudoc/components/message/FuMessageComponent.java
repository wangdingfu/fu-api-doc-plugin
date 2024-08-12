package com.wdf.fudoc.components.message;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.PlatformColors;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.ui.UIUtil;
import cn.fudoc.common.msg.FuMsgManager;
import cn.fudoc.common.msg.bo.FuMsgBO;
import cn.fudoc.common.msg.bo.FuMsgItemBO;
import com.wdf.fudoc.components.listener.FuMsgListener;
import cn.fudoc.common.enumtype.MessageType;
import com.wdf.fudoc.util.ColorUtils;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-12-03 14:24:15
 */
public class FuMessageComponent extends FuHighlightComponent {

    /**
     * 特殊消息默认样式
     */
    private final TextAttributes defaultAttributes;
    /**
     * 消息监听器
     */
    private FuMsgListener fuMsgListener;
    /**
     * 当前选中的子消息
     */
    private FuMsgItemBO currentMsg;
    /**
     * 当前显示的主消息
     */
    private FuMsgBO message;

    /**
     * true-当前鼠标在特殊消息上
     */
    private boolean myMouseHover;

    /**
     * true-当前鼠标点击了特殊消息
     */
    public boolean myMousePressed;


    @Override
    protected boolean isMouseHover() {
        return myMouseHover;
    }

    @Override
    protected boolean isMousePressed() {
        return myMousePressed;
    }

    @Override
    protected String getCurrentMsgId() {
        if (Objects.nonNull(currentMsg)) {
            return currentMsg.getMsgId();
        }
        return null;
    }


    public FuMessageComponent() {
        this(null);
    }

    public FuMessageComponent(FuMsgBO fuMsgBO) {
        this(fuMsgBO, JBColor.DARK_GRAY);
    }

    public FuMessageComponent(FuMsgBO fuMsgBO, Color background) {
        this(fuMsgBO, PlatformColors.BLUE, background, PlatformColors.BLUE);
    }

    public FuMessageComponent(FuMsgBO fuMsgBO, final Color textForegroundColor, final Color textBackgroundColor, final Color textEffectColor) {
        defaultAttributes = UIManager.getLookAndFeel().getName().contains("Darcula") || UIUtil.isUnderIntelliJLaF() ? new CustomTextAttributes(textBackgroundColor) : new TextAttributes(textForegroundColor, textBackgroundColor, textEffectColor, null, Font.PLAIN);
        //设置消息文本
        setMsg(fuMsgBO);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setOpaque(false);
    }


    /**
     * 添加消息监听器
     */
    public void addMsgListener(FuMsgListener fuMsgListener) {
        this.fuMsgListener = fuMsgListener;
    }


    /**
     * 触发指定消息点击事件
     *
     * @param fuMsgItemBO 点击的消息
     */
    public void doClick(FuMsgItemBO fuMsgItemBO) {
        if (Objects.nonNull(this.fuMsgListener)) {
            this.fuMsgListener.clickMsgEvent(message.getMsgId(), fuMsgItemBO);
        }
    }

    /**
     * 判断当前消息是否为特殊消息
     */
    private boolean isOnLink(HighlightedRegion region) {
        return region != null && Objects.nonNull(region.textAttributes);
    }


    /**
     * 设置消息
     */
    public void setMsg(FuMsgBO fuMsgBO) {
        this.message = fuMsgBO;
        initMessage();
    }


    /**
     * 切换消息
     */
    public void switchMsg() {
        setMsg(nextMsg());
    }


    /**
     * 自动获取下一条消息
     */
    public FuMsgBO nextMsg() {
        FuMsgBO fuMsgBO = FuMsgManager.nextMsg();
        if (Objects.nonNull(message) && message.getMsgId().equals(fuMsgBO.getMsgId())) {
            fuMsgBO = nextMsg();
        }
        return fuMsgBO;
    }

    /**
     * 初始化消息
     */
    private void initMessage() {
        //清空上一条消息
        super.clear();
        applyFont();
        List<FuMsgItemBO> messageItemList;
        String myText = FuStringUtils.EMPTY;
        if (Objects.nonNull(this.message) && CollectionUtils.isNotEmpty(messageItemList = this.message.getItemList())) {
            //循环处理每一段消息
            for (FuMsgItemBO fuMsgItemBO : messageItemList) {
                JBColor color = ColorUtils.getColor(fuMsgItemBO);
                TextAttributes textAttributes = null;
                if (MessageType.isUnNormal(fuMsgItemBO.getMsgType())) {
                    textAttributes = Objects.isNull(color) ? defaultAttributes : new TextAttributes(color, color, JBUI.CurrentTheme.Link.Foreground.PRESSED, EffectType.LINE_UNDERSCORE, Font.PLAIN);
                }
                addHighlighter(fuMsgItemBO, textAttributes);
            }
            //设置内容
            myText = highlightedRegionList.stream().map(m -> m.getFuMsgItemBO().getText()).collect(Collectors.joining());
        }
        super.setMyText(myText);
        updateOnTextChange();
    }


    /**
     * 鼠标操作触发的事件
     *
     * @param e the mouse event
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        //获取当前光标所在位置上的消息
        final HighlightedRegion region = findRegionByX(e.getX());
        if (e.getID() == MouseEvent.MOUSE_ENTERED && isOnLink(region)) {
            myMouseHover = true;
            currentMsg = region.fuMsgItemBO;
            repaint();
        } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
            setCursor(null);
            myMouseHover = false;
            myMousePressed = false;
            currentMsg = null;
            repaint();
        } else if (UIUtil.isActionClick(e, MouseEvent.MOUSE_PRESSED) && isOnLink(region)) {
            myMousePressed = true;
            currentMsg = region.getFuMsgItemBO();
            repaint();
        } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            if (myMousePressed && isOnLink(region)) {
                doClick(currentMsg);
            }
            myMousePressed = false;
            currentMsg = null;
            repaint();
        }
        super.processMouseEvent(e);
    }

    /**
     * 鼠标移动触发的事件
     *
     * @param e the <code>MouseEvent</code>
     */
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_MOVED) {
            HighlightedRegion regionByX = findRegionByX(e.getX());
            boolean needRepaint = false;
            FuMsgItemBO fuMsgItemBO;
            if (Objects.nonNull(regionByX) && Objects.nonNull(fuMsgItemBO = regionByX.getFuMsgItemBO())) {
                myMouseHover = MessageType.isUnNormal(fuMsgItemBO.getMsgType());
                if (Objects.isNull(currentMsg) || !currentMsg.getMsgId().equals(fuMsgItemBO.getMsgId())) {
                    currentMsg = fuMsgItemBO;
                    needRepaint = true;
                }
            }
            setCursor(myMouseHover ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : null);
            if (!isOnLink(regionByX)) {
                setCursor(null);
                needRepaint = needRepaint || myMouseHover;
                myMouseHover = false;
            }
            if (needRepaint) {
                repaint();
            }
        }
        super.processMouseMotionEvent(e);
    }


    /**
     * 自定义消息样式
     */
    private final class CustomTextAttributes extends TextAttributes {
        private CustomTextAttributes(Color textBackgroundColor) {
            super(null, textBackgroundColor, null, null, Font.PLAIN);
        }

        @Override
        public Color getForegroundColor() {
            return !isEnabled() ? UIManager.getColor("Label.disabledForeground") : myMousePressed ? JBUI.CurrentTheme.Link.Foreground.PRESSED : myMouseHover ? JBUI.CurrentTheme.Link.Foreground.HOVERED : JBUI.CurrentTheme.Link.Foreground.ENABLED;
        }

        @Override
        public Color getEffectColor() {
            return getForegroundColor();
        }

        @Override
        public EffectType getEffectType() {
            return !isEnabled() || myMouseHover || myMousePressed ? EffectType.LINE_UNDERSCORE : null;
        }

        @Override
        public void setForegroundColor(Color color) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setEffectColor(Color color) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setEffectType(EffectType effectType) {
            throw new UnsupportedOperationException();
        }
    }


    private void applyFont() {
        setFont(UIUtil.getLabelFont());
    }

    private void updateOnTextChange() {
        final JComponent parent = (JComponent) getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
        adjustSize();
    }

    protected void adjustSize() {
        final Dimension preferredSize = getPreferredSize();
        setMinimumSize(preferredSize);
    }
}
