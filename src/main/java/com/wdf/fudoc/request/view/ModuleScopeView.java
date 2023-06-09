package com.wdf.fudoc.request.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.graph.option.IconCellRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.popup.list.IconListPopupRenderer;
import com.intellij.util.ui.table.IconTableCellRenderer;
import com.wdf.fudoc.components.JLabelListRendererComponent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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

    public ModuleScopeView(@Nullable Project project) {
        super(project, true);
        setTitle(TITLE);
        this.rootPanel = new JPanel(new BorderLayout());
        Module[] modules = ModuleManager.getInstance(project).getModules();
        List<String> collect = Arrays.stream(modules).map(Module::getName).collect(Collectors.toList());
        this.moduleComboBox = new ComboBox(collect.toArray(new String[0]));
        for (String s : collect) {
            this.moduleComboBox.setSelectedItem(s);
        }
        this.moduleComboBox.setEditable(true);
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
