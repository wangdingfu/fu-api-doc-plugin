package com.wdf.fudoc.components;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.PlatformColors;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.components.listener.FuMsgListener;
import com.wdf.fudoc.request.constants.enumtype.MessageStyle;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import com.wdf.fudoc.components.test.HighlightableComponent;
import com.wdf.fudoc.components.test.HighlightedText;
import com.wdf.fudoc.components.test.HighlightedRegion;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-11-27 15:23:10
 */
public class FuMsgComponent extends HighlightableComponent {

    private HighlightedText myHighlightedText;

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

    private boolean myMouseHover;

    public boolean myMousePressed;

    @Override
    protected boolean getMyMousePressed() {
        return this.myMousePressed;
    }

    public FuMsgComponent() {
        this(null);
    }

    public FuMsgComponent(FuMsgBO fuMsgBO) {
        this(fuMsgBO, UIUtil.getLabelBackground());
    }

    public FuMsgComponent(FuMsgBO fuMsgBO, Color background) {
        this(fuMsgBO, PlatformColors.BLUE, background, PlatformColors.BLUE);
    }

    public FuMsgComponent(FuMsgBO fuMsgBO, final Color textForegroundColor, final Color textBackgroundColor, final Color textEffectColor) {
        defaultAttributes = StartupUiUtil.isUnderDarcula() || UIUtil.isUnderIntelliJLaF() ? new CustomTextAttributes(textBackgroundColor) : new TextAttributes(textForegroundColor, textBackgroundColor, textEffectColor, null, Font.PLAIN);
        enforceBackgroundOutsideText(textBackgroundColor);
        //设置消息文本
        setMsg(fuMsgBO);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setOpaque(false);
    }

    /**
     * 设置消息
     */
    public void setMsg(FuMsgBO fuMsgBO) {
        if (Objects.isNull(fuMsgBO)) {
            return;
        }
        this.message = fuMsgBO;
        initMessage();

    }

    /**
     * 初始化消息
     */
    private void initMessage() {
        if (Objects.nonNull(this.message)) {
            applyFont();
            myHighlightedText = new HighlightedText();
            for (FuMsgItemBO fuMsgItemBO : this.message.getItemList()) {
                TextAttributes textAttributes = null;
                if (!MessageType.NORMAL.getCode().equals(fuMsgItemBO.getMsgType())) {
                    String style = fuMsgItemBO.getStyle();
                    MessageStyle messageStyle;
                    if (StringUtils.isBlank(style) || Objects.isNull(messageStyle = MessageStyle.getEnum(style))) {
                        textAttributes = defaultAttributes;
                    } else {
                        textAttributes = createAttribute(messageStyle);
                    }
                }
                myHighlightedText.appendText(fuMsgItemBO, textAttributes);
            }
            myHighlightedText.applyToComponent(this);
            updateOnTextChange();
        }
    }


    /**
     * 获取当前点击的消息id
     */
    public String getCurrentMsgId() {
        if (Objects.nonNull(currentMsg)) {
            return currentMsg.getMsgId();
        }
        return null;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        adjustSize();
    }


    @Override
    protected void processMouseEvent(MouseEvent e) {
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

    public void addMsgListener(FuMsgListener fuMsgListener) {
        this.fuMsgListener = fuMsgListener;
    }

    public void doClick(FuMsgItemBO fuMsgItemBO) {
        if (Objects.nonNull(this.fuMsgListener)) {
            this.fuMsgListener.clickMsgEvent(message.getMsgId(), fuMsgItemBO);
        }
    }


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
            if (needRepaint) {
                repaint();
            }
        }
        super.processMouseMotionEvent(e);
    }


    private boolean isOnLink(HighlightedRegion region) {
        return region != null && Objects.nonNull(region.textAttributes);
    }


    @Override
    public void setText(@Nullable @Nls String text) {
        applyFont();
        super.setText(text);
        updateOnTextChange();
    }


    public String getText() {
        return myHighlightedText.getText();
    }


    private void updateOnTextChange() {
        final JComponent parent = (JComponent) getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
        adjustSize();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        applyFont();
    }

    private void applyFont() {
        setFont(UIUtil.getLabelFont());
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleHyperlinkLabel();
        }
        return accessibleContext;
    }

    protected class AccessibleHyperlinkLabel extends AccessibleHighlightable implements AccessibleAction {
        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.HYPERLINK;
        }

        @Override
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override
        public int getAccessibleActionCount() {
            return 1;
        }

        @Override
        public String getAccessibleActionDescription(int i) {
            if (i == 0) {
                return UIManager.getString("AbstractButton.clickText");
            }
            return null;
        }

        @Override
        public boolean doAccessibleAction(int i) {
            if (i == 0) {
                //触发消息
                doClick(currentMsg);
                return true;
            } else {
                return false;
            }
        }
    }


    public TextAttributes createAttribute(MessageStyle messageStyle) {
        if (MessageStyle.ORANGE.equals(messageStyle)) {
            return new TextAttributes(JBColor.ORANGE, UIUtil.getLabelBackground(), JBColor.ORANGE, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        }
        if (MessageStyle.GOLD.equals(messageStyle)) {
            Color color = new JBColor(new Color(234, 116, 0), new Color(234, 116, 0));
            return new TextAttributes(color, UIUtil.getLabelBackground(), color, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        }
        if(MessageStyle.GITEE.equals(messageStyle)){
            Color color = new JBColor(new Color(254, 115, 0), new Color(254, 115, 0));
            return new TextAttributes(color, UIUtil.getLabelBackground(), color, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        }
        if(MessageStyle.GITHUB.equals(messageStyle)){
            Color color = new JBColor(new Color(63, 68, 73), new Color(63, 68, 73));
            return new TextAttributes(color, UIUtil.getLabelBackground(), color, EffectType.LINE_UNDERSCORE, Font.PLAIN);
        }
        return new CustomTextAttributes(UIUtil.getLabelBackground());
    }


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


    protected void adjustSize() {
        final Dimension preferredSize = getPreferredSize();
        setMinimumSize(preferredSize);
    }
}

