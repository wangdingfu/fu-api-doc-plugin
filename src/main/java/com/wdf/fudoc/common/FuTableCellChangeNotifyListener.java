package com.wdf.fudoc.common;

/**
 * swing中JTable的单元格属性改变后通知监听器
 *
 * @author wangdingfu
 * @date 2022-08-13 17:49:38
 */
public interface FuTableCellChangeNotifyListener {


    /**
     * 当单元格中的属性被编辑后(值有变动)调用该方法
     *
     * @param row         编辑单元格的row
     * @param column      编辑单元格的column
     * @param beforeValue 编辑单元格之前的值
     * @param afterValue  编辑单元格后的值
     */
    void notify(int row, int column, String beforeValue, String afterValue);
}
