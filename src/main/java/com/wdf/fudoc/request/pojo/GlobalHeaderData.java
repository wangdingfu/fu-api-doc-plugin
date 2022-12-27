package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.request.constants.enumtype.HeaderScope;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-12-07 22:17:31
 */
@Getter
@Setter
public class GlobalHeaderData extends KeyValueTableBO {

    /**
     * 模块范围
     */
    private HeaderScope headerScope;

    /**
     * 指定模块集合
     */
    private List<String> moduleIdList;
}
