package com.wdf.apidoc.assemble.handler.impl;

import com.wdf.apidoc.assemble.handler.ParamValueHandler;
import com.wdf.apidoc.constant.enumtype.ParamValueType;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.apidoc.pojo.desc.BaseInfoDesc;
import com.wdf.apidoc.pojo.desc.MethodInfoDesc;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Author wangdingfu
 * @Description 接口标题获取接口实现
 * @Date 2022-06-19 22:25:18
 */
public class FuDocTitleValueHandler implements ParamValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.METHOD_TITLE;
    }

    @Override
    public String getParamValue(BaseInfoDesc baseInfoDesc) {

        //获取swagger注解


        //获取注释内容返回
        ApiDocCommentData commentData = baseInfoDesc.getCommentData();
        if(Objects.nonNull(commentData)){
            return commentData.getCommentTitle();
        }
        return StringUtils.EMPTY;
    }
}
