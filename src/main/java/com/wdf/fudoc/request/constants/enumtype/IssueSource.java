package com.wdf.fudoc.request.constants.enumtype;

/**
 * @author wangdingfu
 * @date 2023-06-25 16:12:51
 */
public enum IssueSource {
    GITEE("gitee","Gitee"),
    GITHUB("github","Github"),
    ;

    public final String myActionID;
    public final String myActionName;

    IssueSource(String myActionID, String myActionName) {
        this.myActionID = myActionID;
        this.myActionName = myActionName;
    }
}
