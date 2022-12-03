package com.wdf.fudoc.components.message;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author wangdingfu
 * @date 2022-11-30 14:16:07
 */
public class FuMsgManager {

    private static final double DEFAULT_WEIGHT = 1D;
    private static final double WEIGHT1 = 3D;

    /**
     * 消息ID集合 用户防止消息被重复添加
     */
    private static final Set<String> MSG_IDS = Sets.newHashSet();

    /**
     * 带有权重的随机消息对象
     */
    private static final WeightRandom<FuMsgBO> WEIGHT_RANDOM = new WeightRandom<>();

    static {
        //系统定义好的一些消息
        addMsg(buildFuMsg("按下esc键可以快速退出当前窗口"));
        addMsg(buildFuMsg("发送请求后会自动保存当前请求记录"));
        addMsg(buildFuMsg("点击左侧图标可以切换下一条消息"));
        addMsg(buildFuMsg("建议先请求接口在生成文档(这样你的接口文档实例数据会比较真实)"));
        addMsg(buildFuMsg("当响应结果是文件时 会自动切换到保存文件的页面"));
        addMsg(buildFuMsg("【Fu Doc】目前支持批量编辑请求参数(模仿PostMan的Bulk Edit)"));
        addMsg(buildShare());
        addMsg(buildQuestion());
    }


    /**
     * 添加消息到随机器中
     *
     * @param fuMsgBO 需要提示给用户的消息
     */
    public static void addMsg(FuMsgBO fuMsgBO) {
        Double weight;
        String msgId;
        if (Objects.nonNull(fuMsgBO) && StringUtils.isNotBlank(msgId = fuMsgBO.getMsgId()) && Objects.nonNull(weight = fuMsgBO.getWeight())) {
            if (MSG_IDS.contains(msgId)) {
                return;
            }
            MSG_IDS.add(msgId);
            WEIGHT_RANDOM.add(fuMsgBO, weight);
        }
    }


    /**
     * 获取下一条消息
     * 当只剩下两条消息时 需要调用接口从服务端获取最新的消息
     */
    public static FuMsgBO nextMsg() {
        return WEIGHT_RANDOM.next();
    }

    public static FuMsgBO buildFuMsg(String message) {
        return buildFuMsg(message, null);
    }

    /**
     * 构建消息对象
     */
    public static FuMsgBO buildFuMsg(String message, FuColor fuColor) {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId(IdUtil.nanoId());
        fuMsgBO.setWeight(DEFAULT_WEIGHT);
        fuMsgBO.setItemList(Lists.newArrayList(buildItem(message, fuColor)));
        return fuMsgBO;
    }

    public static FuMsgBO buildQuestion() {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId(IdUtil.nanoId());
        fuMsgBO.setWeight(WEIGHT1);
        fuMsgBO.setItemList(Lists.newArrayList(buildItem("非常希望您能把使用中出现的问题或者建议"),
                buildLinkItem(" 提交到码云 ", UrlConstants.GITEE, FuColor.GITEE),
                buildItem("或"),
                buildLinkItem(" 提交的Github ", UrlConstants.GITHUB, FuColor.GITHUB)));
        return fuMsgBO;
    }


    public static FuMsgBO buildShare() {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId(IdUtil.nanoId());
        fuMsgBO.setWeight(DEFAULT_WEIGHT);
        fuMsgBO.setItemList(Lists.newArrayList(buildItem("如果您觉得"),
                buildLinkItem(" [Fu Doc] ", UrlConstants.DOCUMENT, FuColor.DOCUMENT),
                buildItem("还不错的话 来"),
                buildLinkItem(" 码云 ", UrlConstants.GITEE, FuColor.GITEE),
                buildItem("或"),
                buildLinkItem(" Github ", UrlConstants.GITHUB, FuColor.GITHUB),
                buildItem("上给我一颗小爱心吧")));
        return fuMsgBO;
    }


    private static FuMsgItemBO buildItem(String message) {
        return buildItem(message, null);
    }

    private static FuMsgItemBO buildItem(String message, FuColor fuColor) {
        return buildItem(MessageType.NORMAL.getCode(), message, null, fuColor);
    }

    private static FuMsgItemBO buildLinkItem(String message, String value, FuColor fuColor) {
        return buildItem(MessageType.LINK.getCode(), message, value, fuColor);
    }

    private static FuMsgItemBO buildItem(String msgType, String message, String value, FuColor fuColor) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setMsgId(IdUtil.nanoId());
        fuMsgItemBO.setMsgType(msgType);
        fuMsgItemBO.setText(message);
        fuMsgItemBO.setValue(value);
        if (Objects.nonNull(fuColor)) {
            fuMsgItemBO.setRegularColor(fuColor.getRegularColor());
            fuMsgItemBO.setDarkColor(fuColor.getDarkColor());
        }
        return fuMsgItemBO;
    }
}
