package com.wdf.fudoc.common.exception.report.issue.param;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-07-31 21:52:24
 */
@Getter
@Setter
public class GithubIssueBody {

    private String title;

    private String body;

    private List<String> labels = Lists.newArrayList("bug");
}
