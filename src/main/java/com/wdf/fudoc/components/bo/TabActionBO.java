package com.wdf.fudoc.components.bo;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ActionType;
import com.wdf.fudoc.components.listener.TabBarListener;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * tab工具栏动作对象
 *
 * @author wangdingfu
 * @date 2022-11-05 09:17:19
 */
@Getter
@Setter
public class TabActionBO {

    /**
     * tab标题
     */
    private String title;
    /**
     * tab图标
     */
    private Icon icon;

    /**
     * 当前tab页面展示的组件
     */
    private JComponent mainComponent;

    /**
     * 当前按钮是否选中状态
     */
    private boolean isSelect;

    /**
     * 动作类型
     */
    private ActionType actionType;

    /**
     * 子集动作
     */
    private List<TabActionBO> childList;

    /**
     * 子工具栏面板
     */
    private JPanel childPanel;


    /**
     * 工具栏按钮监听器 当按钮被触发则调用该监听器
     */
    private TabBarListener tabBarListener;

    public TabActionBO(String title, Icon icon, JComponent mainComponent, ActionType actionType) {
        this.title = title;
        this.icon = icon;
        this.mainComponent = mainComponent;
        this.actionType = actionType;
    }

    public TabActionBO(String title, Icon icon, JComponent mainComponent, TabBarListener tabBarListener, ActionType actionType) {
        this.title = title;
        this.icon = icon;
        this.mainComponent = mainComponent;
        this.tabBarListener = tabBarListener;
        this.actionType = actionType;
    }

    public void addChild(TabActionBO tabActionBO) {
        if (Objects.isNull(this.childList)) {
            this.childList = Lists.newArrayList();
        }
        this.childList.add(tabActionBO);
    }
}
