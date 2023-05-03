package com.wdf.fudoc.request.view;

import com.intellij.ui.components.JBTextField;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 一般性授权配置面板（通过界面UI配置）
 * @author wangdingfu
 * @date 2023-05-02 19:27:15
 */
public class GeneralAuthView {

    @Getter
    private final JPanel rootPanel;
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;
    private final JBTextField expireTime = new JBTextField("3600");

    public GeneralAuthView() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.dynamicColumns(), KeyValueTableBO.class);
        JPanel contentPanel = new JPanel(new BorderLayout());
        JPanel northPanel = new JPanel(new BorderLayout());
        //添加过期时间面板
        northPanel.add(createExpireTime(), BorderLayout.EAST);
        //添加表格面板
        contentPanel.add(this.fuTableComponent.createPanel());
        this.rootPanel.add(northPanel, BorderLayout.NORTH);
        this.rootPanel.add(contentPanel, BorderLayout.CENTER);
    }


    /**
     * 创建过期时间配置面板
     */
    private JPanel createExpireTime(){
        JPanel expirePanel = new JPanel(new BorderLayout());
        expirePanel.add(new JLabel("过期时间："), BorderLayout.WEST);
        expirePanel.add(this.expireTime, BorderLayout.CENTER);
        expirePanel.add(new JLabel("秒"), BorderLayout.EAST);
        return expirePanel;
    }


    public List<KeyValueTableBO> getData(){
        return this.fuTableComponent.getDataList();
    }


    /**
     * 设置过期时间
     * @param expireTime 过期时间 单位秒
     */
    public void setExpireTime(Long expireTime) {
        this.expireTime.setText(expireTime + StringUtils.EMPTY);
    }
}