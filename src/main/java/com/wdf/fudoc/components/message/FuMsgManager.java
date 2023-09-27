package com.wdf.fudoc.components.message;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import com.wdf.fudoc.storage.FuDocConfigStorage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
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
     * 系统默认消息
     */
    private static final List<FuMsgBO> DEFAULT_MESSAGE = Lists.newArrayList();

    /**
     * 带有权重的随机消息对象-服务端配置的消息
     */
    private static final WeightRandom<FuMsgBO> WEIGHT_RANDOM = new WeightRandom<>();

    static {
        //系统定义好的一些消息
        DEFAULT_MESSAGE.add((buildCloeIcon()));
        DEFAULT_MESSAGE.add((buildDoc()));
        DEFAULT_MESSAGE.add((buildFuMsg("按下esc键可以快速退出当前窗口")));
        DEFAULT_MESSAGE.add(buildFuMsg("点击左侧图标可以切换下一条消息"));
        DEFAULT_MESSAGE.add(buildFuMsg("非常希望您能帮忙给你身边的同事推广这个插件"));
        DEFAULT_MESSAGE.add(buildShare());
        DEFAULT_MESSAGE.add(buildQuestion());
    }

    private static FuMsgBO buildCloeIcon(){
        return FuMsgBuilder.getInstance().text("可以点我")
                .clickText(" 关闭 ",FuColor.GREEN,"close icon")
                .text("或则")
                .clickText(" 开启 ",FuColor.ORANGE,"open icon")
                .text("Controller左侧图标").build();
    }

    private static FuMsgBO buildDoc(){
        return FuMsgBuilder.getInstance().text("欢迎查看")
                .linkText(" 官方文档 ",FuColor.RED,UrlConstants.DOCUMENT)
                .text("了解插件的详细功能").build();
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
        if (RandomUtil.randomBoolean() && CollectionUtils.isNotEmpty(MSG_IDS)) {
            return WEIGHT_RANDOM.next();
        }
        return DEFAULT_MESSAGE.get(RandomUtil.randomInt(0, DEFAULT_MESSAGE.size()));
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
