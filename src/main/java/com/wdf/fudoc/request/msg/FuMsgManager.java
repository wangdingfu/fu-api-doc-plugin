package com.wdf.fudoc.request.msg;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageStyle;
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
    private static final double WEIGHT1 = 5D;

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
        addMsg(buildFuMsg("点击左侧刷新按钮可以切换消息"));
        addMsg(buildFuMsg("我是一条假消息1"));
        addMsg(buildFuMsg("我是一条假消息2"));
        addMsg(buildFuMsg("我是一条假消息3"));
        addMsg(buildFuMsg("我是一条假消息4"));
        addMsg(buildFuMsg("点击左侧图标可以切换下一条消息"));
        addMsg(buildShare());
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


    /**
     * 构建消息对象
     */
    public static FuMsgBO buildFuMsg(String message) {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId(IdUtil.nanoId());
        fuMsgBO.setWeight(DEFAULT_WEIGHT);
        fuMsgBO.setItemList(Lists.newArrayList(buildItem(message)));
        return fuMsgBO;
    }

    public static FuMsgBO buildShare() {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId(IdUtil.nanoId());
        fuMsgBO.setWeight(WEIGHT1);
        fuMsgBO.setItemList(Lists.newArrayList(buildItem("如果您觉得"),
                buildLinkItem(" Fu Doc ", UrlConstants.DOCUMENT, MessageStyle.ORANGE.getCode()),
                buildItem("还不错的话. 来"),
                buildLinkItem(" Gitee ", UrlConstants.GITEE, null),
                buildItem("或"),
                buildLinkItem(" Github ", UrlConstants.GITHUB, null),
                buildItem("上给我一颗小爱心吧")));
        return fuMsgBO;
    }

    private static FuMsgItemBO buildItem(String message) {
        return buildItem(MessageType.NORMAL.getCode(), message, null, null);
    }

    private static FuMsgItemBO buildLinkItem(String message, String value, String style) {
        return buildItem(MessageType.LINK.getCode(), message, value, style);
    }

    private static FuMsgItemBO buildItem(String msgType, String message, String value, String style) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setMsgId(IdUtil.nanoId());
        fuMsgItemBO.setMsgType(msgType);
        fuMsgItemBO.setText(message);
        fuMsgItemBO.setValue(value);
        fuMsgItemBO.setStyle(style);
        return fuMsgItemBO;
    }
}
