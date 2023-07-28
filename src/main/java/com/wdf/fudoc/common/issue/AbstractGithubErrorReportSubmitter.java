package com.wdf.fudoc.common.issue;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.util.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GitHub issue 报告基类
 *
 * @author wq
 * @date 2022/6/3
 */
public abstract class AbstractGithubErrorReportSubmitter extends AbstractErrorReportSubmitter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");
    private static final String API_BASE_URL = "https://api.github.com";

    /**
     * 获取 GitHub 仓库地址, 即用户名+仓库名
     *
     * @return GitHub 仓库地址
     */
    @NotNull
    protected abstract String getGithubRepo();

    /**
     * 获取汇报者 GitHub token
     *
     * @return 汇报者 GitHub token
     */
    protected abstract String getGithubToken();

    @Override
    protected String getIssueListPageUrl() {
        return UrlConstants.ISSUE;
    }

    @NotNull
    @Override
    protected String generateTextByIssueId(String issueId) {
        return "Github Issue#" + issueId;
    }

    @NotNull
    @Override
    protected String generateUrlByIssueId(String issueId) {
        return UrlConstants.ISSUE + "/" + issueId;
    }

    @NotNull
    @Override
    protected String newIssueByTitleBody(String title, String body) {
        Map<String, Object> json = new HashMap<>(4);
        json.put("title", title);
        json.put("body", body);
        json.put("labels", Lists.newArrayList("bug"));
        HttpRequest httpRequest = HttpUtil.createPost(API_BASE_URL + "/repos/" + getGithubRepo() + "/issues");
        httpRequest.header("Authorization", getGithubToken());
        String result = httpRequest.body(JsonUtil.toJson(json)).execute().body();
        if (StringUtils.isBlank(result)) {
            return StringUtils.EMPTY;
        }
        Object number = JSONUtil.parse(result).getByPath("number");
        return Objects.isNull(number) ? StringUtils.EMPTY : DECIMAL_FORMAT.format(number);
    }

    @SneakyThrows
    @Override
    protected String findIssueByMd5(String throwableMd5) {
        String q = "repo:" + getGithubRepo() + " is:issue in:body " + throwableMd5;
        URLCodec urlCodec = new URLCodec("utf-8");
        String query = "q=" + urlCodec.encode(q) + "&page=1&per_page=1";
        String result = HttpUtil.get(API_BASE_URL + "/search/issues?" + query);
        if (StringUtils.isBlank(result) || !JSONUtil.isTypeJSON(result)) {
            return StringUtils.EMPTY;
        }
        Object value = JSONUtil.parse(result).getByPath("items[0].number");
        return Objects.isNull(value) ? null : DECIMAL_FORMAT.format(value);
    }

}
