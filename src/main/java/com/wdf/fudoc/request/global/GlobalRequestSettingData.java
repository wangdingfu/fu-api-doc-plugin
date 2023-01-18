package com.wdf.fudoc.request.global;

import com.wdf.fudoc.request.pojo.GlobalHeaderData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 【Fu Request】全局设置数据
 *
 * @author wangdingfu
 * @date 2022-12-07 22:16:40
 */
@Getter
@Setter
public class GlobalRequestSettingData {


    /**
     * 全局请求头
     */
    private List<GlobalHeaderData> globalHeaderDataList;
}
