package com.wdf.fudoc.components.tree;

import com.intellij.ui.treeStructure.SimpleTree;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.tree.TreePath;
import java.util.Objects;

/**
 * 表格树组件
 *
 * @author wangdingfu
 * @date 2023-01-07 22:15:35
 */
public class FuTableTreeComponent extends JComboBox<TreePath[]> {

    /**
     * 树控件
     */
    @Getter
    private SimpleTree simpleTree;

    private DefaultListCellRenderer renderer;

    private MetalComboBoxUI metalComboBoxUI;

    public FuTableTreeComponent(SimpleTree simpleTree) {
        this.simpleTree = simpleTree;
        init();
        setTree(simpleTree);
    }

    /**
     * 设置树
     *
     * @param simpleTree JTree
     */
    public void setTree(SimpleTree simpleTree) {
        this.simpleTree = simpleTree;
        if (simpleTree != null) {
            this.setSelectedItem(simpleTree.getSelectionPaths());
            this.setRenderer(this.renderer);
        }
        this.updateUI();
    }


    @Override
    public void setSelectedItem(Object anObject) {
        simpleTree.setSelectionPaths((TreePath[]) anObject);
        getModel().setSelectedItem(anObject);
    }



    @Nullable
    @Override
    public TreePath[] getSelectedItem() {
        return simpleTree.getSelectionPaths();
    }

    @Override
    public void updateUI() {
        ComboBoxUI comboBoxUI = Objects.isNull(metalComboBoxUI) ? (ComboBoxUI)UIManager.getUI(this) : metalComboBoxUI;
        setUI(comboBoxUI);
    }


    private void init() {
        if (Objects.isNull(metalComboBoxUI)) {
            metalComboBoxUI = new MetalJTreeComboBoxUI();
        }
        if (Objects.isNull(renderer)) {
            renderer = new FuTableTreeComboBoxRenderer(this.simpleTree);
        }
    }

    static class MetalJTreeComboBoxUI extends MetalComboBoxUI {
        protected ComboPopup createPopup() {
            return new FuTreePopup(comboBox);
        }
    }
}
