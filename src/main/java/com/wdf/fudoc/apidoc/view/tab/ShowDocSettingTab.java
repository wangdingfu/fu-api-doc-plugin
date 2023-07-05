package com.wdf.fudoc.apidoc.view.tab;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxProjectTableData;
import com.wdf.fudoc.apidoc.sync.data.ShowDocProjectTableData;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuViewListener;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-07-05 18:46:36
 */
public class ShowDocSettingTab implements FuTab, FuViewListener {
    private JRootPane rootPane;
    private JPanel rootPanel;
    private JPanel basicPanel;
    private JTextField domainField;
    private JCheckBox enableBox;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel domainLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginBtn;
    private JPanel mainPanel;

    private final FuTableComponent<ShowDocProjectTableData> projectTable;

    public ShowDocSettingTab() {
        this.basicPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.mainPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.projectTable = FuTableComponent.create(FuTableColumnFactory.showDoc(), ShowDocProjectTableData.class);
        this.mainPanel.add(this.projectTable.createPanel(), BorderLayout.CENTER);
        initRootPane();
    }


    public void initRootPane() {
        this.rootPane = new JRootPane();
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.loginBtn);
    }


    @Override
    public TabInfo getTabInfo() {
        JPanel slidePanel = new JPanel(new BorderLayout());
        ActionLink actionLink = new ActionLink(FuBundle.message("fudoc.sync.showdoc.token.link"), e -> {
            BrowserUtil.browse("https://www.apifox.cn/help/openapi/");
        });
        actionLink.setForeground(FuColor.color6.color());
        slidePanel.add(actionLink, BorderLayout.EAST);
        return FuTabComponent.getInstance("ShowDoc", FuDocIcons.FU_API_SHOW_DOC, this.rootPane).builder(slidePanel);
    }



    @Override
    public void apply() {

    }

    @Override
    public void reset() {

    }
}
