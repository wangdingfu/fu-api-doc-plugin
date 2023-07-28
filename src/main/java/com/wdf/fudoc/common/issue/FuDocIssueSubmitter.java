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
