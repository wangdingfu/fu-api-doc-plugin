package com.wdf.fudoc.common.exception.report.issue;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.exception.IssueException;
import com.wdf.fudoc.common.exception.report.issue.param.GithubIssueBody;
import com.wdf.fudoc.util.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 提交issue至github
 *
 * @author wangdingfu
 * @date 2023-07-31 20:48:42
 */
public class GithubIssueSubmitter extends AbstractIssueSubmitter {
    private static final String API_BASE_URL = "https://api.github.com";

    private static final String CREATE_ISSUE = "/repos/wangdingfu/fudoc/issues";

    private static final String ACCESS_TOKEN = "Bearer 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";


    /**
     * 请求github创建一个issue
     *
     * @param title issue标题
     * @param body  issue内容
     * @return issueId
     */
    @Override
    protected String doCreateIssue(String title, String body) {
        GithubIssueBody issueBody = new GithubIssueBody();
        issueBody.setTitle(title);
        issueBody.setBody(body);
        HttpRequest httpRequest = HttpUtil.createPost(API_BASE_URL + CREATE_ISSUE);
        try {
            String result = httpRequest.header("Authorization", ACCESS_TOKEN)
                    .body(JsonUtil.toJson(issueBody)).timeout(6000).execute().body();
            if (StringUtils.isBlank(result)) {
                return StringUtils.EMPTY;
            }
            Object number = JSONUtil.parse(result).getByPath("number");
            if (Objects.isNull(number)) {
                throw new IssueException("提交issue至Github失败");
            }
            return number.toString();
        } catch (Exception e) {
            //超时或则请求异常
            throw new IssueException("提交Issue至Github网络超时");
        }
    }

    /**
     * 查询issue
     *
     * @param throwableMd5 错误堆栈信息md5字符串
     * @return issueId
     */
    @SneakyThrows
    @Override
    public String findIssue(String throwableMd5) {
        String q = "repo:wangdingfu/fudoc is:issue in:body " + throwableMd5;
        URLCodec urlCodec = new URLCodec("utf-8");
        String query = "q=" + urlCodec.encode(q) + "&page=1&per_page=1";
        String result = HttpUtil.get(API_BASE_URL + "/search/issues?" + query);
        if (StringUtils.isBlank(result) || !JSONUtil.isTypeJSON(result)) {
            return StringUtils.EMPTY;
        }
        Object value = JSONUtil.parse(result).getByPath("items[0].number");
        return Objects.isNull(value) ? null : value.toString();
    }

    /**
     * 组装issue访问地址
     *
     * @param issueId 问题id
     * @return issue访问地址
     */
    @Override
    public String issueUrl(String issueId) {
        if (StringUtils.isBlank(issueId)) {
            return UrlConstants.ISSUE;
        }
        return UrlConstants.ISSUE + "/" + issueId;
    }


    /**
     * 展示issue文本
     *
     * @param issueId 问题id
     * @return issue文本
     */
    @Override
    public String issueText(String issueId) {
        return "Github Issue#" + issueId;
    }


}
