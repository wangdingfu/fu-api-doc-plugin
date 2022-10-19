package com.wdf.fudoc.common.constant;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-10-19 11:05:25
 */
public class TipInfoConstants {

    private static final List<String> TIP_INFO = Lists.newArrayList(
            "按下esc键可以快速退出当前窗口",
            "发送请求后会自动保存当前请求记录",
            "这是一条假消息",
            "【Fu Doc】需要您的支持. 快来Github或Gitee赏个Star吧"
    );


    /**
     * 随机获取一条消息
     */
    public static String get() {
        return TIP_INFO.get(RandomUtil.randomInt(0, TIP_INFO.size()));
    }
}
