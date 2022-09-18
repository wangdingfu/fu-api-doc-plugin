package com.wdf.fudoc.request.factory;

import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-18 19:24:15
 */
public class FuHttpRequestDataFactory {


    public static FuHttpRequestData build(FuDocItemData fuDocItemData) {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        fuHttpRequestData.setApiName(fuDocItemData.getTitle());
        FuRequestData fuRequestData = new FuRequestData();
        RequestType requestType = RequestType.getRequestType(fuDocItemData.getRequestType());
        fuRequestData.setRequestType(requestType);
        fuRequestData.setRequestUrl(fuDocItemData.getUrlList().get(0));
        fuHttpRequestData.setRequest(fuRequestData);
        return fuHttpRequestData;
    }



}
