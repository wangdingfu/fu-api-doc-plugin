package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.IconManager;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.Getter;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-06-26 15:09:56
 */
public class HttpCmdView {

    private final Project project;
    @Getter
    private final VerticalBox verticalBox;
    private final List<String> indexList = Lists.newArrayList();
    @Getter
    private final Map<String, FuHttpRequestData> fuHttpRequestDataMap = new ConcurrentHashMap<>();

    private final Map<String, ActionLink> actionLinkMap = new ConcurrentHashMap<>();


    public HttpCmdView(Project project) {
        this.project = project;
        this.verticalBox = new VerticalBox();
    }


    public void addHttp() {
        addHttp(null, FuHttpRequestDataFactory.buildEmptyHttpRequestData());
    }

    public void addHttp(String key, FuHttpRequestData fuHttpRequestData) {
        if (FuStringUtils.isBlank(key)) {
            int index = indexList.size() + 1;
            key = "#" + index;
        }
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
        this.verticalBox.add(actionLink);
        this.verticalBox.add(Box.createVerticalStrut(5));
        this.verticalBox.revalidate();
    }


    private JPopupMenu buildPopupMenu(String key) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeHttp = new JMenuItem("删除http配置", IconManager.getInstance().getIcon("expui/general/delete.svg", AllIcons.class));
        popupMenu.add(removeHttp);
        removeHttp.addActionListener(e -> {
            indexList.remove(key);
            fuHttpRequestDataMap.remove(key);
            ActionLink actionLink = this.actionLinkMap.remove(key);
            this.verticalBox.remove(actionLink);
            this.verticalBox.revalidate();
        });
        return popupMenu;
    }


}
