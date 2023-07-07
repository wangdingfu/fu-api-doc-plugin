package com.wdf.fudoc.request.execute;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuConsole;
import com.wdf.fudoc.request.constants.enumtype.RequestStatus;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.net.ConnectException;
import java.net.HttpCookie;
import java.util.List;
import java.util.stream.Collectors;


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
    public static void execute(Project project, FuHttpRequestData fuHttpRequestData, FuRequestConfigPO fuRequestConfigPO, FuConsole fuConsole) {
        long start = System.currentTimeMillis();
        //将【Fu Request】请求数据对象转换为http请求数据
        HttpRequest httpRequest = FuHttpRequestBuilder.getInstance(project, fuHttpRequestData, fuRequestConfigPO).builder();
        RequestStatus requestStatus = RequestStatus.FAIL;
        String requestUrl = fuHttpRequestData.getRequest().getRequestUrl();
        try {
            fuConsole.info("开始发起接口【{}】请求", requestUrl);
            HttpResponse httpResponse = httpRequest.execute();
            requestStatus = RequestStatus.SUCCESS;
            FuHttpResponseBuilder.buildSuccessResponse(fuHttpRequestData, httpResponse);
            String content = fuHttpRequestData.getResponse().getContent();
            fuConsole.info("发起接口【{}】请求成功. 共计耗时:{}ms. 响应结果:\n{}",
                    requestUrl, System.currentTimeMillis() - start,
                    JSONUtil.isTypeJSON(content) ? JSONUtil.toJsonPrettyStr(content) : content);
            //将cookie保存在当前项目下
            List<HttpCookie> cookies = httpResponse.getCookies();
            if (CollectionUtils.isNotEmpty(cookies)) {
                fuRequestConfigPO.addCookies(cookies.stream().map(HttpExecutor::buildCookie).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            FuResponseData fuResponseData = FuHttpResponseBuilder.ifNecessaryCreateResponse(fuHttpRequestData);
            log.info("请求接口【{}】异常", requestUrl, e);
            fuConsole.error("请求接口【{}】异常. 异常信息:\n{}", requestUrl, e);
            if (e.getCause() instanceof ConnectException) {
                fuResponseData.setErrorDetail("错误：connect ECONNREFUSED " + httpRequest.getConnection().getUrl().getAuthority());
                fuResponseData.setResponseType(ResponseType.ERR_CONNECTION_REFUSED);
            } else {
                fuResponseData.setErrorDetail(e.getCause().getMessage());
                fuResponseData.setResponseType(ResponseType.ERR_UNKNOWN);
            }
        } finally {
            long time = System.currentTimeMillis() - start;
            log.info("请求接口【{}】完成. 请求状态码：{}. 共计耗时:{}ms", requestUrl, requestStatus, time);
            fuHttpRequestData.setTime(time);
        }
    }

    private static FuCookiePO buildCookie(HttpCookie httpCookie) {
        FuCookiePO fuCookiePO = new FuCookiePO();
        BeanUtil.copyProperties(httpCookie, fuCookiePO);
        return fuCookiePO;
    }


}
