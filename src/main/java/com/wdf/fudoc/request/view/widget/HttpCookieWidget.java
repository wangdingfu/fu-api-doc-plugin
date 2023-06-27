package com.wdf.fudoc.request.view.widget;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.ActionLink;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.HttpCookieView;

import javax.swing.*;
import java.util.Objects;

/**
 * 展示当前项目的cookie
 *
 * @author wangdingfu
 * @date 2023-06-27 11:16:00
 */
public class HttpCookieWidget implements FuWidget {


    private final ActionLink actionLink;

    private HttpCookieView httpCookieView;

    public HttpCookieWidget(Project project) {
        actionLink = new ActionLink("Cookies");
        actionLink.addActionListener(e -> {
            //创建cookie面板
            if (Objects.isNull(httpCookieView)) {
                httpCookieView = new HttpCookieView(project);
            }
            //刷新cookie
            httpCookieView.initData();
            //展示cookie面板
            httpCookieView.show();
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
