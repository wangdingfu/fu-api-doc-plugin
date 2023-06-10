package com.wdf.fudoc.request.po;

import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局请求头｜全局变量
 *
 * @author wangdingfu
 * @date 2023-06-10 21:54:18
 */
@Getter
@Setter
public class GlobalKeyValuePO extends KeyValueTableBO {

    /**
     * 作用范围
     */
    private TreePathBO scope;

}
