package com.wdf.fudoc.components.listener;

/**
 * @author wangdingfu
 * @date 2023-02-21 00:14:48
 */
public interface FuActionListener<T> {


    void doAction(T data);


    default void doActionAfter(T data){

    }


    default void remove(T data){

    }

}
