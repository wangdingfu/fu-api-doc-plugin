package com.wdf.fudoc.view.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-04 21:05:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarPanelBO {

    /**
     * bar显示的文本
     */
    private String text;

    /**
     * bar显示的图标
     */
    private Icon icon;

    /**
     * 当前按钮是否选中状态
     */
    private boolean isSelect;

    /**
     * 选中后显示的目标面板
     */
    private JPanel targetPanel;

}
