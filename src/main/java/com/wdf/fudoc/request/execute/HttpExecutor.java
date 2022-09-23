package com.wdf.fudoc.request.execute;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wdf.fudoc.request.constants.enumtype.RequestStatus;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.extern.slf4j.Slf4j;


/**
 * @author wangdingfu
 * @date 2022-09-23 17:59:39
 */
@Slf4j
public class HttpExecutor {

    /**
     * 执行请求
     *
     * @param fuHttpRequestData 发起http所需的数据对象
     */
    public static void execute(FuHttpRequestData fuHttpRequestData) {
        long start = System.currentTimeMillis();
        HttpRequest httpRequest = FuHttpRequestBuilder.getInstance(fuHttpRequestData).builder();
        RequestStatus requestStatus = RequestStatus.FAIL;
        String requestUrl = fuHttpRequestData.getRequest().getRequestUrl();
        try {
            HttpResponse httpResponse = httpRequest.execute();
            requestStatus = RequestStatus.SUCCESS;
            FuHttpResponseBuilder.buildResponse(fuHttpRequestData, httpResponse);
        } catch (Exception e) {
            log.info("请求接口【{}】异常", requestUrl, e);
        } finally {
            long time = System.currentTimeMillis() - start;
            log.info("请求接口【{}】完成. 请求状态码：{}. 共计耗时:{}ms", requestUrl, requestStatus, time);
            fuHttpRequestData.setTime(time);
        }
    }


}
