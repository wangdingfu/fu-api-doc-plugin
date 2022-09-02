package com.wdf.fudoc.view.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.ide.IdeBundle;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.FuDocMessageBundle;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.view.TestRequestFrom;
import com.wdf.fudoc.view.bo.KeyValueBO;
import com.wdf.fudoc.view.components.FuEditorComponent;
import com.wdf.fudoc.view.components.FuTableComponent;
import icons.FuDocIcons;
import lombok.Getter;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ButtonUI;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2022-08-25 22:07:47
 */
public class FuRequestWindow extends SimpleToolWindowPanel implements DataProvider {

    @Getter
    private final JPanel rootPanel;

    private Project project;


    public FuRequestWindow(@NotNull Project project) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.project = project;
        initToolbar();
        this.rootPanel = new TestRequestFrom().getRootPanel();
        setContent(this.rootPanel);
    }

    private void initToolbar() {
        final ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                (DefaultActionGroup) actionManager.getAction("fu.doc.request.tool.window.action"), true);
        actionToolbar.setTargetComponent(getToolbar());
        setToolbar(actionToolbar.getComponent());
    }

//    private void initRootPanel() {
//
//        JPanel headPanel = new JPanel(new BorderLayout(0, 0));
//        JPanel requestPanel = new JPanel(new BorderLayout(0, 0));
//        ComboBox<Object> objectComboBox1 = new ComboBox<>(Lists.newArrayList("GET", "POST", "DELETE", "PUT").toArray());
//        objectComboBox1.setBackground(new JBColor(new Color(70, 130, 180), new Color(70, 130, 180)));
//        requestPanel.add(objectComboBox1, BorderLayout.WEST);
//        JBTextField jbTextField = new JBTextField();
//        jbTextField.setColumns(45);
//        requestPanel.add(jbTextField);
//        JButton send = new JButton("Send", FuDocIcons.FU_REQUEST_SEND);
//        requestPanel.add(send, BorderLayout.EAST);
//        headPanel.add(requestPanel, BorderLayout.NORTH);
//
//        JBTabbedPane requestBodyPanel = new JBTabbedPane();
//        FuTableComponent<KeyValueBO> tableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(new KeyValueBO("key1","value1")), KeyValueBO.class);
//        requestBodyPanel.addTab("Headers", FuDocIcons.FU_REQUEST_HEADER, tableComponent.createPanel());
//        requestBodyPanel.addTab("Params", FuDocIcons.FU_REQUEST_PARAMS, new FuEditorComponent(JsonFileType.INSTANCE, "").getMainPanel());
//        requestBodyPanel.addTab("Body", FuDocIcons.FU_REQUEST_BODY, new FuEditorComponent(JsonFileType.INSTANCE, "").getMainPanel());
//        headPanel.add(requestBodyPanel, BorderLayout.CENTER);
//
//        JPanel responsePanel = new JPanel(new BorderLayout(0, 0));
//        JBTabbedPane tabbedPane = new JBTabbedPane();
//        tabbedPane.addTab("Body", FuDocIcons.FU_REQUEST_BODY, new FuEditorComponent(JsonFileType.INSTANCE, "").getMainPanel());
//        tabbedPane.addTab("Row", new FuEditorComponent(JsonFileType.INSTANCE, "").getMainPanel());
//        responsePanel.add(SeparatorFactory.createSeparator(FuDocMessageBundle.message("response"), null));
//        responsePanel.add(tabbedPane);
//
//        // 分割器
//        Splitter splitter = new Splitter(true, 0.5F);
//        splitter.setFirstComponent(headPanel);
//        splitter.setSecondComponent(responsePanel);
//        this.rootPanel.add(splitter, BorderLayout.CENTER);
//    }


    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if ("FuRequestWindow".equals(dataId)) {
            return this;
        }
        return super.getData(dataId);
    }
}
