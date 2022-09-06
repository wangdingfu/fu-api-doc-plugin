package com.wdf.fudoc.view.toolwindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.wdf.fudoc.view.TestRequestFrom;
import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
        this.rootPanel = new TestRequestFrom(project).getRootPanel();
        setContent(this.rootPanel);
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
