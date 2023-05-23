package com.wdf.fudoc.request.http;

/**
 * 发起Http请求数据对象
 *
 * @author wangdingfu
 * @date 2023-05-21 22:56:57
 */
public interface FuRequest {

    /**
     * 获取当前请求记录持久化路径
     *
     * @return http文件路径（${projectPath}/.idea/fudoc/api/${moduleName}/${controllerName}）
     */
    String getPath();


    /**
     * 获取请求记录持久化文件名称
     *
     * @return 接口持久化到磁盘的文件名称 目前指定为方法名
     */
    String getHttpFileName();


    /**
     * 获取需要持久化到磁盘的http文件内容
     *
     * @return http文件内容
     */
    String httpContent();

}
