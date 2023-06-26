package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-06-26 15:09:56
 */
public class HttpCmdView {

    private final Project project;
    private final JPanel rootPanel;
    private final List<String> indexList = Lists.newArrayList();
    @Getter
    private final Map<String, FuHttpRequestData> fuHttpRequestDataMap = new ConcurrentHashMap<>();

    private final Map<String, ActionLink> actionLinkMap = new ConcurrentHashMap<>();


    public HttpCmdView(JPanel rootPanel, Project project, String title) {
        this.project = project;
        this.rootPanel = rootPanel;
        this.rootPanel.add(buildTitle(title));
        this.rootPanel.add(Box.createVerticalStrut(5));
    }


    private JComponent buildTitle(String title) {
        VerticalBox verticalBox = new VerticalBox();
        JLabel titleLabel = new JLabel();
        Font font = titleLabel.getFont();
        titleLabel.setFont(new Font(font.getFontName(), Font.BOLD, 14));
        titleLabel.setText(title);
        verticalBox.add(titleLabel);
        verticalBox.add(Box.createVerticalStrut(10));
        JLabel addLabel = new JLabel(AllIcons.General.Add);
        // 更改光标类型为小手
        addLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addHttp();
            }
        });
        verticalBox.add(addLabel);
        return verticalBox;
    }

    public void addHttp() {
        addHttp(FuHttpRequestDataFactory.buildEmptyHttpRequestData());
    }

    public void addHttp(FuHttpRequestData fuHttpRequestData) {
        int index = indexList.size() + 1;
        String key = "#" + index;
        indexList.add(key);
        fuHttpRequestDataMap.put(key, fuHttpRequestData);
        ActionLink actionLink = new ActionLink("配置http请求 ( " + key + " )", e -> {
            //弹框配置http请求
            HttpDialogView httpDialogView = new HttpDialogView(project, null, fuHttpRequestData, true);
            httpDialogView.show();
        });
        actionLink.setBorder(JBUI.Borders.empty(1, 14, 3, 1));
        JPopupMenu jPopupMenu = buildPopupMenu(key);
        actionLink.setComponentPopupMenu(jPopupMenu);
        actionLink.addMouseListener(new PopupHandler() {
            @Override
            public void invokePopup(Component comp, int x, int y) {
                jPopupMenu.show(comp, x, y);
            }
        });
        this.actionLinkMap.put(key, actionLink);
        this.rootPanel.add(actionLink);
        this.rootPanel.add(Box.createVerticalStrut(5));
        this.rootPanel.revalidate();
    }


    private JPopupMenu buildPopupMenu(String key) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeHttp = new JMenuItem("删除http配置", AllIcons.General.Remove);
        popupMenu.add(removeHttp);
        removeHttp.addActionListener(e -> {
            indexList.remove(key);
            fuHttpRequestDataMap.remove(key);
            ActionLink actionLink = this.actionLinkMap.remove(key);
            this.rootPanel.remove(actionLink);
            this.rootPanel.revalidate();
        });
        return popupMenu;
    }


}
