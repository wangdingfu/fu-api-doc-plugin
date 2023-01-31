package com.wdf.fudoc.components.listener;

/**
 * @author wangdingfu
 * @date 2023-01-30 11:33:11
 */
public class FuTableDisableListener<T> implements FuTableListener<T> {

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
