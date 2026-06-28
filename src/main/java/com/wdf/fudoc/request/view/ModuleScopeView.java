package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.JLabelListRendererComponent;
import com.wdf.fudoc.util.FuDocUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.multi.MultiComboBoxUI;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-06-09 21:18:19
 */
public class ModuleScopeView extends DialogWrapper {

    private static final String TITLE = "设置Module";

    private final JPanel rootPanel;

    private final JComboBox<String> moduleComboBox;

    public ModuleScopeView(@Nullable Project project, List<String> selectedList) {
        super(project, true);
        setTitle(TITLE);
        this.rootPanel = new JPanel(new BorderLayout());
        List<String> allModuleNameList = FuDocUtils.getAllModuleNameList(project);
        this.moduleComboBox = new ComboBox(allModuleNameList.toArray(new String[0]));
        this.moduleComboBox.setUI(new MultiComboBoxUI());
        for (String s : selectedList) {
            this.moduleComboBox.setSelectedItem(s);
        }
        this.moduleComboBox.setRenderer(new JLabelListRendererComponent());
        this.rootPanel.add(this.moduleComboBox, BorderLayout.CENTER);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }


    public List<String> getSelected() {
        Object[] selectedObjects = this.moduleComboBox.getSelectedObjects();
        return Arrays.stream(selectedObjects).map(Object::toString).collect(Collectors.toList());
    }

}
