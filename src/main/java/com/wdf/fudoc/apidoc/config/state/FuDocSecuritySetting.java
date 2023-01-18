package com.wdf.fudoc.apidoc.config.state;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.util.CodeUtils;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Fu Doc 存储配置内容
 *
 * @author wangdingfu
 * @date 2022-08-06 01:17:22
 */
@Data
@State(name = "fuDocSecuritySetting", storages = {@Storage("fsp.xml")})
public class FuDocSecuritySetting implements PersistentStateComponent<FuDocSecuritySetting> {
    private static final int TWO_DAY = 60 * 60 * 24 * 2;

    /**
     * 过期时长
     */
    private long time;

    /**
     * 唯一码
     */
    private String uniqId = CodeUtils.getCpuId();

    /**
     * 未读提示时间集合
     */
    private Map<String, Long> tipMap = new ConcurrentHashMap<>();

    /**
     * 已读的公告
     */
    private List<String> tipList = Lists.newArrayList();

    public void addTipId(String tipId, boolean isRead) {
        if (isRead) {
            tipList.add(tipId);
        } else {
            tipMap.put(tipId, DateUtil.currentSeconds());
        }
    }

    public boolean isShow(String tipId) {
        if (tipList.contains(tipId)) {
            //已读了 则后续永远不展示
            return false;
        }
        Long time = tipMap.get(tipId);
        if (Objects.isNull(time)) {
            //没有 则需要展示
            return true;
        }
        //未读超过两日则继续提示
        return time + TWO_DAY > DateUtil.currentSeconds();
    }


    public static FuDocSecuritySetting getInstance() {
        return ServiceHelper.getService(FuDocSecuritySetting.class);
    }


    @Override
    public @Nullable FuDocSecuritySetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull FuDocSecuritySetting state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
