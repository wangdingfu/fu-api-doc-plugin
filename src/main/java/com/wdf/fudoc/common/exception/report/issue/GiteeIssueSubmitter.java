package com.wdf.fudoc.common.exception.report.issue;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.request.constants.enumtype.IssueSource;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * issue提交至Gitee
 *
 * @author wangdingfu
 * @date 2023-07-31 20:56:55
 */
public class GiteeIssueSubmitter extends AbstractIssueSubmitter {

    private static final String CREATE_ISSUE_URL = "https://gitee.com/api/v5/repos/wdfu/issues";

    /**
     * 创建issue
     *
     * @param title issue标题
     * @param body  issue内容
     * @return issueId
     */
    @Override
    protected String doCreateIssue(String title, String body) {
        HttpRequest httpRequest = HttpUtil.createPost(CREATE_ISSUE_URL);
        String result = httpRequest.form("repo", "fu-api-doc-plugin")
                .form("title", title)
                .form("body", body)
                .form("labels", "bug")
                .form("access_token", getAccessToken(IssueSource.GITEE.myActionID)).execute().body();
        if (StringUtils.isBlank(result) || !JSONUtil.isTypeJSON(result)) {
            return StringUtils.EMPTY;
        }
        Object number = JSONUtil.parse(result).getByPath("number");
        return Objects.isNull(number) ? StringUtils.EMPTY : number.toString();
    }

    @Override
    protected String getAccessToken() {
        return "5de56c1ca1c71bd295da6eae84eb7419";
    }

    /**
     * Gitee暂未提供搜索issue的接口 直接返回空字符串
     *
     * @param throwableText 错误堆栈信息
     * @return IssueId
     */
    @Override
    public String findIssue(String throwableText) {
        return StringUtils.EMPTY;
    }

    @Override
    public String issueUrl(String issueId) {
        if (StringUtils.isBlank(issueId)) {
            return UrlConstants.GITEE_ISSUE;
        }
        return UrlConstants.GITEE_ISSUE + "/" + issueId;
    }

    @Override
    public String issueText(String issueId) {
        return "Gitee Issue#" + issueId;
    }


}
