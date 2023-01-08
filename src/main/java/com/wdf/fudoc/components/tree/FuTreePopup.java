package com.wdf.fudoc.components.tree;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

/**
 * @author wangdingfu
 * @date 2023-01-07 22:27:02
 */
public class FuTreePopup extends JPopupMenu implements ComboPopup {
    protected FuTableTreeComponent comboBox;
    protected JBScrollPane scrollPane;

    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;

    public FuTreePopup(JComboBox<?> comboBox) {
        this.comboBox = (FuTableTreeComponent) comboBox;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BorderLayout());
        setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
        SimpleTree simpleTree = this.comboBox.getSimpleTree();
        if (simpleTree != null) {
            scrollPane = new JBScrollPane(simpleTree);
            scrollPane.setBorder(null);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    public void show() {
        updatePopup();
        show(comboBox, 0, comboBox.getHeight());
        comboBox.getSimpleTree().requestFocus();
    }

    @Override
    public void hide() {
        setVisible(false);
        comboBox.firePropertyChange("popupVisible", true, false);
    }


    @Override
    public JList getList() {
        return new JBList();
    }

    public MouseMotionListener getMouseMotionListener() {
        if (mouseMotionListener == null) {
            mouseMotionListener = new MouseMotionAdapter() {
            };
        }
        return mouseMotionListener;
    }

    public KeyListener getKeyListener() {
        return null;
    }

    public void uninstallingUI() {
    }

    /**
     * Implementation of ComboPopup.getMouseListener().
     *
     * @return a <code>MouseListener</code> or null
     * @see ComboPopup#getMouseListener
     */
    public MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = new FuTreePopup.InvocationMouseHandler();
        }
        return mouseListener;
    }

    protected void updatePopup() {
        setPreferredSize(new Dimension(comboBox.getSize().width, 200));
        Object selectedObj = comboBox.getSelectedItem();
        if (selectedObj != null) {
            TreePath[] tp = (TreePath[]) selectedObj;
            comboBox.getSimpleTree().setSelectionPaths(tp);
        }
    }

    protected class InvocationMouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
                return;
            }
            if (comboBox.isEditable()) {
                Component comp = comboBox.getEditor().getEditorComponent();
                if ((!(comp instanceof JComponent))
                        || ((JComponent) comp).isRequestFocusEnabled()) {
                    comp.requestFocus();
                }
            } else if (comboBox.isRequestFocusEnabled()) {
                comboBox.requestFocus();
            }
            show();
        }

    }
}