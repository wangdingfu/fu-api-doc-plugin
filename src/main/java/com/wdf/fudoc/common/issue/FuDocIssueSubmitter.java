package com.wdf.fudoc.common.issue;

import org.jetbrains.annotations.NotNull;

/**
 * 错误报告处理类
 *
 * @author wenquan
 * @date 2022/5/13
 */
public class FuDocIssueSubmitter extends AbstractGithubErrorReportSubmitter {

    @NotNull
    protected String getGithubRepo() {
        return "wangdingfu/fudoc";
    }

    @Override
    protected String getGithubToken() {
        return "Bearer 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";
    }

    @Override
    protected String getExampleIssueId() {
        return "1";
    }

}
