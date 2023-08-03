package com.wdf.fudoc.common.exception.report.issue;

/**
 * @author wangdingfu
 * @date 2023-07-31 20:52:07
 */
public interface IssueSubmitter {


    /**
     * 创建issue
     *
     * @param throwableText  错误栈信息
     * @param message        一般为栈的第一行信息
     * @param additionalInfo 用户输入信息
     * @return 提交issue id
     */
    String createIssue(String throwableText, String message, String additionalInfo);


    /**
     * 根据错误信息查询问题是否已经提交过
     *
     * @param throwableText 错误堆栈信息
     * @return 问题issueId
     */
    String findIssue(String throwableText);


    /**
     * issue页面url
     *
     * @param issueId 问题id
     * @return issue页面地址
     */
    String issueUrl(String issueId);


    /**
     * issue展示的文本
     *
     * @param issueId 问题id
     * @return 报告问题页面展示问题的文本
     */
    String issueText(String issueId);
}
