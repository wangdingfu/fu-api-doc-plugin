package com.wdf.fudoc.common.exception.report;

import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-07-30 21:07:09
 */
public class FuErrorReportSubmitter extends ErrorReportSubmitter {


    /**
     * 获取报告问题按钮名称
     *
     * @return Report To WangDingFu
     */
    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return FuBundle.message("fudoc.error.report");
    }

    /**
     * 提交问题
     *
     * @param events          a non-empty sequence of error descriptors.
     * @param additionalInfo  additional information provided by a user.
     * @param parentComponent UI component to use as a parent in any UI activity from a submitter.
     * @param consumer        a callback to be called after sending is finished (or failed).
     * @return 是否提交成功
     */
    @Override
    public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer<? super SubmittedReportInfo> consumer) {
        try {
            IdeaLoggingEvent event = events[0];
            String throwableText = event.getThrowableText();
            if (StringUtils.isBlank(throwableText)) {
                return false;
            }
            String message = event.getMessage();
            if (StringUtils.isBlank(message)) {
                message = throwableText.substring(0, throwableText.indexOf("\r\n"));
            }
            if (StringUtils.isBlank(additionalInfo)) {
                additionalInfo = "";
            }
            //校验当前问题是否提交过 如果提交过则查询该问题
            SubmittedReportInfo reportInfo = createOrFindIssue(throwableText, message, additionalInfo);
            consumer.consume(reportInfo);
            return !SubmittedReportInfo.SubmissionStatus.FAILED.equals(reportInfo.getStatus());
        } catch (Exception e) {
            consumer.consume(new SubmittedReportInfo("", "error: " + e.getMessage(), SubmittedReportInfo.SubmissionStatus.FAILED));
            return false;
        }
    }


    /**
     * 创建或则查询issue
     *
     * @param throwableText  错误栈信息
     * @param message        一般为栈的第一行信息
     * @param additionalInfo 用户输入信息
     * @return 提交issue id
     */
    private SubmittedReportInfo createOrFindIssue(String throwableText, String message, String additionalInfo) {
        String issueId = findIssue(throwableText);
        if (StringUtils.isNotBlank(issueId)) {
            FuDocNotification.notifyInfo("fudoc.issue.repeat");
            return new SubmittedReportInfo(issueUrl(issueId), issueText(issueId), SubmittedReportInfo.SubmissionStatus.DUPLICATE);
        }
        //提交问题
        issueId = doCreateIssue(throwableText, message, additionalInfo);
        if (StringUtils.isBlank(issueId)) {
            return new SubmittedReportInfo(issueUrl(issueId), issueText(issueId), SubmittedReportInfo.SubmissionStatus.NEW_ISSUE);
        }
        return new SubmittedReportInfo("", "submit issue timeout", SubmittedReportInfo.SubmissionStatus.FAILED);
    }


    /**
     * 创建issue
     *
     * @param throwableText  错误栈信息
     * @param message        一般为栈的第一行信息
     * @param additionalInfo 用户输入信息
     * @return 提交issue id
     */
    private String doCreateIssue(String throwableText, String message, String additionalInfo) {
        return StringUtils.EMPTY;
    }


    /**
     * 根据错误信息查询问题是否已经提交过
     *
     * @param throwableText 错误堆栈信息
     * @return 问题issueId
     */
    private String findIssue(String throwableText) {
        return StringUtils.EMPTY;
    }


    /**
     * issue页面url
     *
     * @param issueId 问题id
     * @return issue页面地址
     */
    private String issueUrl(String issueId) {
        return UrlConstants.ISSUE + "/" + issueId;
    }


    /**
     * issue展示的文本
     *
     * @param issueId 问题id
     * @return 报告问题页面展示问题的文本
     */
    private String issueText(String issueId) {
        return "Github Issue#" + issueId;
    }
}
