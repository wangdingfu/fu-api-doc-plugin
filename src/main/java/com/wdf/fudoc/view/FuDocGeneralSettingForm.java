package com.wdf.fudoc.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2022-08-07 23:33:30
 */
@Getter
@Setter
public class FuDocGeneralSettingForm {
    private JPanel root;
    private JLabel label1;
    private JPanel panel1;
    private JTable table1;
    private JBList<Map.Entry<String, String>> typeMapList;
    private Project project;

    public FuDocGeneralSettingForm(Project project) {
        this.project = project;
        Object[] columns={"字段一","字段二"};
        Object[][] objectData = new Object[][]{{"bbb","ddd"}};
        this.table1.setModel(new DefaultTableModel(objectData,columns));
        createUIComponents();
    }


    private void createUIComponents() {
        typeMapList = new JBList<>(new CollectionListModel<>(Lists.newArrayList()));
        typeMapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        typeMapList.setCellRenderer(new ListCellRendererWrapper<Map.Entry<String, String>>() {
            @Override
            public void customize(JList list, Map.Entry<String, String> value, int index, boolean selected, boolean hasFocus) {
                setText(value.getKey() + " -> " + value.getValue());
            }
        });

        typeMapList.setEmptyText("请添加单词映射");
        typeMapList.setSelectedIndex(0);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(typeMapList);
        toolbarDecorator.setAddAction(button -> {

        });
        toolbarDecorator.setRemoveAction(anActionButton -> {

        });
        this.panel1 = toolbarDecorator.createPanel();
    }
}
