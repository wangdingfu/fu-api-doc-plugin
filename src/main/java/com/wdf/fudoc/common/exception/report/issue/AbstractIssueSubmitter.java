package com.wdf.fudoc.common.exception.report.issue;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.SystemInfo;
import cn.fudoc.common.base.CommonResult;
import com.wdf.fudoc.common.FuDocRender;
import cn.fudoc.common.constants.ApiUrl;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.exception.report.issue.param.IssueBody;
import org.apache.commons.codec.digest.DigestUtils;
import com.wdf.fudoc.util.FuStringUtils;
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
        String result = doSendRequest(ApiUrl.ACCESS_TOKEN, param);
        String accessToken = getData(result);
        return FuStringUtils.isBlank(accessToken) ? getAccessToken() : accessToken;
    }


    private static final String BASE_URL = "http://www.fudoc.cn:9090";
    private static final String PRIMARY_KEY = "dfe68b77d54943fc8d481c6ae80a2a9d";

    public static String doSendRequest(String apiUrl, Map<String, Object> paramMap) {
        //对请求参数加密
        byte[] request = SecureUtil.aes(PRIMARY_KEY.getBytes()).encrypt(JSONUtil.toJsonStr(paramMap));
        HttpResponse httpResponse = null;
        try {
            //请求获取最新通知信息
            httpResponse = HttpRequest.post(BASE_URL + apiUrl).timeout(6000).contentType(ContentType.JSON.getValue()).body(request).execute();
            return httpResponse.body();
        } catch (Exception e) {
            return null;
        } finally {
            if (Objects.nonNull(httpResponse)) {
                httpResponse.close();
            }
        }
    }

    public static <T> T getData(String result) {
        CommonResult<T> commonResult = JSONUtil.toBean(result, new TypeReference<>() {
        }, true);
        if (Objects.nonNull(commonResult) && commonResult.getCode() == 200) {
            return commonResult.getData();
        }
        return null;
    }

}
