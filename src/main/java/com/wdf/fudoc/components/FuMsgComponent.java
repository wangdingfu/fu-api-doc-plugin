package com.wdf.fudoc.components;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.PlatformColors;
import com.intellij.util.ui.StartupUiUtil;
import com.intellij.util.ui.UIUtil;
import lombok.Getter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import com.wdf.fudoc.components.test.HighlightableComponent;
import com.wdf.fudoc.components.test.HighlightedText;
import com.wdf.fudoc.components.test.HighlightedRegion;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-11-27 15:23:10
 */
public class FuMsgComponent extends HighlightableComponent {

    private HighlightedText myHighlightedText;
    private final List<HyperlinkListener> myListeners = ContainerUtil.createLockFreeCopyOnWriteList();
    private boolean myUseIconAsLink;
    private final TextAttributes myAnchorAttributes;
    private HyperlinkListener myHyperlinkListener;

    private String currentMsgId;

    private boolean myMouseHover;

    public boolean myMousePressed;

    @Override
    protected boolean getMyMousePressed() {
        return this.myMousePressed;
    }

    public FuMsgComponent() {
        this("");
    }

    public FuMsgComponent(String text) {
        this(text, UIUtil.getLabelBackground());
    }

    public FuMsgComponent(String text, Color background) {
        this(text, PlatformColors.BLUE, background, PlatformColors.BLUE);
    }

    public FuMsgComponent(String text, final Color textForegroundColor, final Color textBackgroundColor, final Color textEffectColor) {
        myAnchorAttributes = StartupUiUtil.isUnderDarcula() || UIUtil.isUnderIntelliJLaF() ? new CustomTextAttributes(textBackgroundColor) :
                new TextAttributes(textForegroundColor, textBackgroundColor, textEffectColor, null, Font.PLAIN);

        enforceBackgroundOutsideText(textBackgroundColor);
        //设置链接文本
        setLinkText(text);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setOpaque(false);
    }


    public void setLinkText(String text) {
        applyFont();
        myHighlightedText = new HighlightedText();
        myHighlightedText.appendText("1", " 这是一条消息", null);
        myHighlightedText.appendText("2", " [给我点赞] ", myAnchorAttributes);
        myHighlightedText.appendText("3", "或者", null);
        myHighlightedText.appendText("4", " [加入我们] ", myAnchorAttributes);
        myHighlightedText.applyToComponent(this);
        updateOnTextChange();
    }


    public String getCurrentMsgId() {
        return this.currentMsgId;
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
            currentMsgId = region.getMsgId();
            repaint();
        } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
            setCursor(null);
            myMouseHover = false;
            myMousePressed = false;
            currentMsgId = null;
            repaint();
        } else if (UIUtil.isActionClick(e, MouseEvent.MOUSE_PRESSED) && isOnLink(region)) {
            myMousePressed = true;
            currentMsgId = region.getMsgId();
            repaint();
        } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            if (myMousePressed && isOnLink(region)) {
                fireLinkEvent(e, currentMsgId);
            }
            myMousePressed = false;
            currentMsgId = null;
            repaint();
        }
        super.processMouseEvent(e);
    }


    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_MOVED) {
            boolean onLink = isOnLink(findRegionByX(e.getX()));
            boolean needRepaint = myMouseHover != onLink;
            myMouseHover = onLink;
            setCursor(myMouseHover ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : null);

            if (needRepaint) {
                repaint();
            }
        }
        super.processMouseMotionEvent(e);
    }


    private boolean isOnLink(HighlightedRegion region) {
        return region != null && region.textAttributes == myAnchorAttributes;
    }

    @Override
    protected void processComponentKeyEvent(KeyEvent event) {
        if (event.getModifiers() == 0 && event.getKeyCode() == KeyEvent.VK_SPACE) {
            event.consume();
            fireHyperlinkEvent(event);
        }
    }


    @Override
    public void setText(@Nullable @Nls String text) {
        applyFont();
        myUseIconAsLink = false;
        super.setText(text);
        updateOnTextChange();
    }

    public void setHyperlinkTarget(@NonNls @Nullable final String url) {
        if (myHyperlinkListener != null) {
            removeHyperlinkListener(myHyperlinkListener);
        }
        if (url != null) {
            myHyperlinkListener = e -> BrowserUtil.browse(url);
            addHyperlinkListener(myHyperlinkListener);
            setIcon(AllIcons.Ide.External_link_arrow);
            setIconAtRight(true);
        }
    }

    public void addHyperlinkListener(HyperlinkListener listener) {
        myListeners.add(listener);
    }

    public void removeHyperlinkListener(HyperlinkListener listener) {
        myListeners.remove(listener);
    }

    public String getText() {
        return myHighlightedText.getText();
    }

    protected void fireHyperlinkEvent(@Nullable InputEvent inputEvent) {
        HyperlinkEvent e = new HyperlinkEvent(this, HyperlinkEvent.EventType.ACTIVATED, null, null, null, inputEvent);
        for (HyperlinkListener listener : myListeners) {
            listener.hyperlinkUpdate(e);
        }
    }

    protected void fireLinkEvent(@Nullable InputEvent inputEvent, String msgId) {
        HyperlinkEvent e = new HyperlinkEvent(this, HyperlinkEvent.EventType.ACTIVATED, null, msgId, null, inputEvent);
        for (HyperlinkListener listener : myListeners) {
            listener.hyperlinkUpdate(e);
        }
    }

    public void doClick() {
        fireHyperlinkEvent(null);
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
                doClick();
                return true;
            } else {
                return false;
            }
        }
    }

    private final class CustomTextAttributes extends TextAttributes {
        private CustomTextAttributes(Color textBackgroundColor) {
            super(null, textBackgroundColor, null, null, Font.PLAIN);
        }

        @Override
        public Color getForegroundColor() {
            return !isEnabled() ? UIManager.getColor("Label.disabledForeground") :
                    myMousePressed ? JBUI.CurrentTheme.Link.Foreground.PRESSED :
                            myMouseHover ? JBUI.CurrentTheme.Link.Foreground.HOVERED :
                                    JBUI.CurrentTheme.Link.Foreground.ENABLED;
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

