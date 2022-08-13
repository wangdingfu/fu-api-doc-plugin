package com.wdf.fudoc.helper;

import com.wdf.fudoc.common.FuTableCellChangeNotifyListener;
import com.wdf.fudoc.listener.FuTableCellListener;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Swing中JTable帮助类
 *
 * @author wangdingfu
 * @date 2022-08-13 17:33:08
 */
public class FuTableHelper {


    public static void addChangeListener(JTable jTable, FuTableCellChangeNotifyListener notifyListener) {
        jTable.addPropertyChangeListener("tableCellEditor", new FuTableCellListener(jTable, notifyListener));
    }
}
