package com.wdf.fudoc.common.po;

import com.wdf.fudoc.components.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 自定义表格配置
     */
    private Map<String, List<KeyValueTableBO>> customTableConfigMap = new HashMap<>();

}
