package com.wdf.fudoc.request.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-07-21 17:53:12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePopupMenuItem {


    private Icon icon;

    private String showName;

    private boolean canSelect;

    public BasePopupMenuItem(Icon icon, String showName) {
        this.icon = icon;
        this.showName = showName;
        this.canSelect = true;
    }
}
