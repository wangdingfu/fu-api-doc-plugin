package com.wdf.fudoc.common.exception.report.issue;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.SystemInfo;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.constant.ApiUrl;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.exception.report.issue.param.IssueBody;
import com.wdf.fudoc.start.RequestManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wangdingfu
 * @date 2023-07-31 21:17:22
 */
public abstract class AbstractIssueSubmitter implements IssueSubmitter {


    /**
     * 创建issue
     *
     * @param title issue标题
     * @param body  issue内容
     * @return issueId
     */
    protected abstract String doCreateIssue(String title, String body);

    /**
     * 获取默认授权token
     */
    protected abstract String getAccessToken();


    /**
     * 创建一个issue
     *
     * @param throwableText  错误栈信息
     * @param message        一般为栈的第一行信息
     * @param additionalInfo 用户输入信息
     * @return issue id
     */
    @Override
    public String createIssue(String throwableText, String message, String additionalInfo) {
        return doCreateIssue("[Report From Idea] " + message, buildIssueBody(throwableText, message, additionalInfo));
    }


    /**
     * 构建issue内容
     *
     * @param throwableText  错误栈信息
     * @param message        一般为栈的第一行信息
     * @param additionalInfo 用户输入信息
     * @return issue内容
     */
    private String buildIssueBody(String throwableText, String message, String additionalInfo) {
        IssueBody issueBody = new IssueBody();
        //必要的基本信息
        issueBody.setThrowableText(throwableText);
        issueBody.setMessage(message);
        issueBody.setAdditionalInfo(additionalInfo);
        issueBody.setIssueMd5(DigestUtils.md5Hex(throwableText).toUpperCase());

        //环境信息
        ApplicationInfoEx appInfo = ApplicationInfoEx.getInstanceEx();
        Properties properties = System.getProperties();
        issueBody.setFullApplicationName(appInfo.getFullApplicationName());
        issueBody.setEditionName(ApplicationNamesInfo.getInstance().getEditionName());
        issueBody.setBuild(appInfo.getBuild().asString());
        issueBody.setBuildDate(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(appInfo.getBuildDate().getTime()));
        issueBody.setJavaRuntimeVersion(properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown")));
        issueBody.setOsArch(properties.getProperty("os.arch", ""));
        issueBody.setVmName(properties.getProperty("java.vm.name", "unknown"));
        issueBody.setVmVendor(properties.getProperty("java.vendor", "unknown"));
        issueBody.setOsInfo(SystemInfo.getOsNameAndVersion());
        issueBody.setEncoding(Charset.defaultCharset().displayName());

        //插件相关信息
        IdeaPluginDescriptor pluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId(FuDocConstants.ID));
        if (Objects.nonNull(pluginDescriptor)) {
            issueBody.setPluginName(pluginDescriptor.getName());
            issueBody.setPluginVersion(pluginDescriptor.getVersion());
        }
        return FuDocRender.render(issueBody, "issue.ftl");
    }


    protected String getAccessToken(String type) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        String result = RequestManager.doSendRequest(ApiUrl.ACCESS_TOKEN, param);
        String accessToken = RequestManager.getData(result);
        return StringUtils.isBlank(accessToken) ? getAccessToken() : accessToken;
    }


}
