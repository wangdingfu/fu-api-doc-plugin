package com.wdf.fudoc.assemble.handler.impl;

import com.wdf.fudoc.assemble.handler.ParamValueHandler;
import com.wdf.fudoc.constant.enumtype.ParamValueType;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.desc.BaseInfoDesc;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @Author wangdingfu
 * @Description 接口详细内容值获取实现
 * @Date 2022-06-19 22:28:06
 */
public class FuDocDetailInfoValueHandler implements ParamValueHandler {
    @Override
    public ParamValueType getParamValueType() {
        return ParamValueType.METHOD_DETAIL_INFO;
    }

    @Override
    public String getParamValue(FuDocContext fuDocContext, BaseInfoDesc baseInfoDesc) {
        ApiDocCommentData commentData = baseInfoDesc.getCommentData();
        if (Objects.nonNull(commentData)) {
            return commentData.getCommentDetailInfo();
        }
        return StringUtils.EMPTY;
    }
}
