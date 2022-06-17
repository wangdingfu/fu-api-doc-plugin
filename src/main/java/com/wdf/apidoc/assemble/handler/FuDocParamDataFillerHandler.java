package com.wdf.apidoc.assemble.handler;

import com.wdf.apidoc.constant.enumtype.ParamDataType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @Author wangdingfu
 * @Description 参数值填充接口
 * @Date 2022-06-17 21:15:35
 */
public interface FuDocParamDataFillerHandler {

    /**
     * 获取参数数据类型
     */
    ParamDataType getParamDataType();


    /**
     * 获取参数的值（用于渲染到接口文档上）
     *
     * @param objectInfoDesc 解析后的对象
     * @return 参数对应的值
     */
    String doGetValue(ObjectInfoDesc objectInfoDesc);

}
