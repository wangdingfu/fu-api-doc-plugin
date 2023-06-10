package com.wdf.fudoc.components.listener;

import javax.swing.*;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-06-10 20:05:31
 */
public interface FuFiltersListener<T> {

    List<T> getAllElements();

    List<T> getSelectedElements();


    String getElementText(T data);

    Icon getElementIcon(T data);

    void setSelected(T data, boolean isMark);
}
