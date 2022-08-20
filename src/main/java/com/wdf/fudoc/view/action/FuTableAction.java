package com.wdf.fudoc.view.action;

import java.util.List;

/**
 * Fu Table组件需要传入的行为 主要用户操作表格的数据
 *
 * @author wangdingfu
 * @date 2022-08-20 17:00:08
 */
public interface FuTableAction<T> {

    /**
     * 获取table组件需要展示的数据
     *
     * @return table的数据集合
     */
    List<T> getDataList();

    /**
     * 将table的数据提供出来由业务去持久化
     *
     * @param dataList 当前table中的数据
     */
    void setDataList(List<T> dataList);

    /**
     * table中添加一条数据
     *
     * @param data table中的一行数据
     */
    void addRow(T data);

    /**
     * 从table中移除指定行数据
     *
     * @param row 指定行数
     */
    void removeRow(Integer row);

    /**
     * 从持久化对象中获取某一行数据
     *
     * @param row 指定行数
     */
    void getRow(Integer row);

}
