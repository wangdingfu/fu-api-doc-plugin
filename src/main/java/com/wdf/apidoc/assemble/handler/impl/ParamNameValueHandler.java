package com.wdf.apidoc.assemble.handler.impl;

import com.wdf.apidoc.assemble.handler.BaseParamFieldValueHandler;
import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.ValidateAnnotationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author wangdingfu
 * @Description 参数值获取器
 * @Date 2022-06-18 22:37:09
 */
public class ParamNameValueHandler extends BaseParamFieldValueHandler {


    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.PARAM_NAME;
    }

    @Override
    protected String doGetParamValue(ObjectInfoDesc objectInfoDesc) {
        //获取swagger注解标识的参数名

        //获取注释参数名
        String name = objectInfoDesc.getName();
        if (StringUtils.isNotBlank(name)) {
            return name;
        }
        //获取校验注解信息截取出字段名
        String validMessage = ValidateAnnotationUtils.getValidMessage(objectInfoDesc);
        if (StringUtils.isNotBlank(validMessage)) {
            return validMessage.replace("不能为空", "");
        }
        return "";
    }


}
