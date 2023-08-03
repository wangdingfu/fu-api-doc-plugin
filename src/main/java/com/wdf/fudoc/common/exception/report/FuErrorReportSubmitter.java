package com.wdf.fudoc.common.exception.report;

import cn.hutool.core.text.StrFormatter;
import com.intellij.notification.BrowseNotificationAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.exception.IssueException;
import com.wdf.fudoc.common.exception.report.issue.GiteeIssueSubmitter;
import com.wdf.fudoc.common.exception.report.issue.GithubIssueSubmitter;
import com.wdf.fudoc.common.exception.report.issue.IssueSubmitter;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.request.constants.enumtype.IssueSource;
import com.wdf.fudoc.storage.FuDocConfigStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-07-30 21:07:09
 */
@Slf4j
public class FuErrorReportSubmitter extends ErrorReportSubmitter {

    private final IssueSubmitter giteeIssueSubmitter = new GiteeIssueSubmitter();
    private final IssueSubmitter githubIssueSubmitter = new GithubIssueSubmitter();

    private final Map<String, String> issueIdMap = new ConcurrentHashMap<>();

    @Override
    @Nullable
    public String getPrivacyNoticeText() {
        String issueTo = FuDocConfigStorage.INSTANCE.readData().getIssueTo();
        return StrFormatter.format("由于未接入{}登录, 建议在提交issue之后 <a href='{}'>点击进入</a> 页面留言以获得 issue 进展通知!<br/>", issueTo, getIssueSubmitter(issueTo).issueUrl(issueIdMap.get(issueTo)));
    }

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
    public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer<SubmittedReportInfo> consumer) {
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
        String issueTo = FuDocConfigStorage.INSTANCE.readData().getIssueTo();
        IssueSubmitter issueSubmitter = getIssueSubmitter(issueTo);
        String issueId = issueSubmitter.findIssue(throwableText);
        if (StringUtils.isNotBlank(issueId)) {
            issueIdMap.put(issueTo, issueId);
            String issueUrl = issueSubmitter.issueUrl(issueId);
            String linkText = FuBundle.message(MessageConstants.ISSUE_LINK_TEXT);
            FuDocNotification.notifyInfo(FuBundle.message("fudoc.issue.repeat"), new BrowseNotificationAction(linkText, issueUrl));
            return new SubmittedReportInfo(issueSubmitter.issueUrl(issueId), issueSubmitter.issueText(issueId), SubmittedReportInfo.SubmissionStatus.DUPLICATE);
        }
        String errorText = "submit issue timeout";
        try {
            //提交问题
            issueId = issueSubmitter.createIssue(throwableText, message, additionalInfo);
        } catch (IssueException e) {
            log.info("提交Issue失败", e);
            errorText = e.getMessage();
            if (issueSubmitter instanceof GithubIssueSubmitter) {
                ifNecessarySubmitToGitee(throwableText, message, additionalInfo);
            }
        }
        if (StringUtils.isNotBlank(issueId)) {
            issueIdMap.put(issueTo, issueId);
            return new SubmittedReportInfo(issueSubmitter.issueUrl(issueId), issueSubmitter.issueText(issueId), SubmittedReportInfo.SubmissionStatus.NEW_ISSUE);
        }
        return new SubmittedReportInfo("", errorText, SubmittedReportInfo.SubmissionStatus.FAILED);
    }


    private void ifNecessarySubmitToGitee(String throwableText, String message, String additionalInfo) {
        String actionText = FuBundle.message("fudoc.issue.gitee");
        String errorMessage = FuBundle.message("fudoc.issue.github.fail");
        FuDocNotification.notifyError(errorMessage, new AnAction(actionText) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                FuDocConfigStorage instance = FuDocConfigStorage.INSTANCE;
                instance.readData().setIssueTo(IssueSource.GITEE.myActionID);
                instance.saveData();
                String issue = giteeIssueSubmitter.createIssue(throwableText, message, additionalInfo);
                if (StringUtils.isNotBlank(issue)) {
                    String issueUrl = giteeIssueSubmitter.issueUrl(issue);
                    String title = FuBundle.message("fudoc.issue.gitee.success");
                    String linkText = FuBundle.message(MessageConstants.ISSUE_LINK_TEXT);
                    FuDocNotification.notifyInfo(title, new BrowseNotificationAction(linkText, issueUrl));
                }
            }
        });
    }

    protected IssueSubmitter getIssueSubmitter(String issueTo) {
        return IssueSource.GITHUB.myActionID.equals(issueTo) ? githubIssueSubmitter : giteeIssueSubmitter;
    }


}
