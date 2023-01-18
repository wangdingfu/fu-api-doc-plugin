package com.wdf.fudoc.common;

import com.intellij.openapi.application.ApplicationManager;

/**
 * @author wangdingfu
 * @descption: 获取Service帮助类
 * @date 2022-05-30 23:06:39
 */
public class ServiceHelper {


    /**
     * 从应用中获取service
     *
     * @param clazz service clazz
     * @param <T>   service类型
     * @return service实例
     */
    public static <T> T getService(Class<T> clazz) {
        return ApplicationManager.getApplication().getService(clazz);
    }
}
