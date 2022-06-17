package com.wdf.apidoc.assemble.handler.impl;

import com.wdf.apidoc.assemble.handler.FuDocParamDataFillerHandler;
import com.wdf.apidoc.constant.enumtype.ParamDataType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;

/**
 * @Author wangdingfu
 * @Description 参数名称填充处理器
 * @Date 2022-06-17 22:44:04
 */
public class ParamNameFillerHandler implements FuDocParamDataFillerHandler {
    @Override
    public ParamDataType getParamDataType() {
        return ParamDataType.PARAM_NAME;
    }


    /**
     * 获取参数名称
     * @param objectInfoDesc 解析后的对象
     * @return 渲染到接口文档的参数名称
     */
    @Override
    public String doGetValue(ObjectInfoDesc objectInfoDesc) {

        //判断是否有第三方注解指定参数名（例如）

        //判断是否存在校验注解 从校验注解上获取参数名称

        String name = objectInfoDesc.getName();

        return null;
    }
}
