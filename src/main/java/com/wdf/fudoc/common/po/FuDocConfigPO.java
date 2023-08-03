package com.wdf.fudoc.common.po;

import com.wdf.fudoc.request.constants.enumtype.IssueSource;
import lombok.Getter;
import lombok.Setter;

/**
 * [fudoc]配置信息持久化
 *
 * @author wangdingfu
 * @date 2023-07-08 22:40:12
 */
@Getter
@Setter
public class FuDocConfigPO {

    /**
     * issue同步 默认同步至Github
     */
    private String issueTo = IssueSource.GITHUB.myActionID;

}
