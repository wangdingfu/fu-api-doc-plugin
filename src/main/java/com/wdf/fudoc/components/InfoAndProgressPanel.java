package com.wdf.fudoc.components;

import com.intellij.openapi.util.*;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.ex.ProgressIndicatorEx;
import com.intellij.openapi.wm.impl.status.*;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.GuiUtils;
import com.intellij.util.ui.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public final class InfoAndProgressPanel extends JPanel implements CustomStatusBarWidget {

    private final StatusPanel myInfoPanel = new StatusPanel();
    private final JPanel myRefreshAndInfoPanel = new JPanel();


    private final List<ProgressIndicatorEx> myOriginals = new ArrayList<>();
    private final Map<InlineProgressIndicator, ProgressIndicatorEx> myInlineToOriginal = new HashMap<>();

    private final JLabel myRefreshIcon = new JLabel(new AnimatedIcon.FS());



    public InfoAndProgressPanel() {
        setOpaque(false);
        setBorder(JBUI.Borders.empty());

        myRefreshIcon.setVisible(true);

        myRefreshAndInfoPanel.setLayout(new BorderLayout());
        myRefreshAndInfoPanel.setOpaque(false);
        myRefreshAndInfoPanel.add(myRefreshIcon, BorderLayout.WEST);
        myRefreshAndInfoPanel.add(myInfoPanel, BorderLayout.CENTER);
        myRefreshAndInfoPanel.add(myInfoPanel, BorderLayout.CENTER);

        setRefreshVisible(true);
        setLayout(new InlineLayout());
        add(myRefreshAndInfoPanel);

        myRefreshAndInfoPanel.revalidate();
        myRefreshAndInfoPanel.repaint();

    }


    @Override
    public @NotNull String ID() {
        return "fuInfo";
    }

    @Override
    public WidgetPresentation getPresentation() {
        return null;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public void dispose() {
        setRefreshVisible(false);
        synchronized (myOriginals) {
            restoreEmptyStatus();
            for (InlineProgressIndicator indicator : myInlineToOriginal.keySet()) {
                Disposer.dispose(indicator);
            }
            myInlineToOriginal.clear();
        }
        GuiUtils.removePotentiallyLeakingReferences(myRefreshIcon);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }


    public void setText(String text) {
        myInfoPanel.updateText( text);
    }

    void setRefreshVisible(boolean visible) {
        UIUtil.invokeLaterIfNeeded(() -> myRefreshIcon.setVisible(visible));
    }

    private void restoreEmptyStatus() {
        removeAll();
        setLayout(new BorderLayout());
        add(myRefreshAndInfoPanel, BorderLayout.CENTER);

        myRefreshAndInfoPanel.revalidate();
        myRefreshAndInfoPanel.repaint();
    }

    private static class InlineLayout extends AbstractLayoutManager {
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension result = new Dimension();
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Dimension size = parent.getComponent(i).getPreferredSize();
                result.width += size.width;
                result.height = Math.max(result.height, size.height);
            }
            return result;
        }

        @Override
        public void layoutContainer(Container parent) {
            Component infoPanel = parent.getComponent(0);
            Dimension size = parent.getSize();
            infoPanel.setBounds(0, 0, size.width , size.height);
        }
    }
}
