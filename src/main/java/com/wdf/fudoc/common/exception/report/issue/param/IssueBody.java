package com.wdf.fudoc.common.exception.report.issue.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * @author wangdingfu
 * @date 2023-07-31 21:34:06
 */
@Getter
@Setter
public class IssueBody {

    private String throwableText;
    private String message;
    private String additionalInfo;
    private String issueMd5;


    private String fullApplicationName;
    private String editionName;
    private String build;
    private String buildDate;
    private String javaRuntimeVersion;
    private String osArch;
    private String vmName;
    private String vmVendor;
    private String osInfo;
    private String encoding;

    private String pluginName;
    private String pluginVersion;
}
