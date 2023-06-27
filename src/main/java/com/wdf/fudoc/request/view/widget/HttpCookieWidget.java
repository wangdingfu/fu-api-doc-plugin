package com.wdf.fudoc.request.view.widget;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.ActionLink;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpCookieView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 展示当前项目的cookie
 *
 * @author wangdingfu
 * @date 2023-06-27 11:16:00
 */
public class HttpCookieWidget implements FuWidget {


    private final ActionLink actionLink;

    public HttpCookieWidget(Project project) {
        actionLink = new ActionLink("Cookies", e -> {

        });
        actionLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                // 获取屏幕上的绝对坐标
                Point point = mouseEvent.getLocationOnScreen();
                //创建cookie面板
                HttpCookieView httpCookieView = new HttpCookieView(project);
                //展示cookie面板
                httpCookieView.getWindow().setLocation(point);
                httpCookieView.show();
            }
        });
    }

    @Override
    public JComponent getComponent() {
        return actionLink;
    }

    @Override
    public void initData(FuHttpRequestData fuHttpRequestData) {

    }
}
