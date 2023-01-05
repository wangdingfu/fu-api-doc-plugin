package com.wdf.fudoc.apidoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.common.ServiceHelper;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Fu Doc 存储配置内容
 *
 * @author wangdingfu
 * @date 2022-08-06 01:17:22
 */
@Data
@State(name = "fuDocSecuritySetting", storages = {@Storage("fsp.xml")})
public class FuDocSecuritySetting implements PersistentStateComponent<FuDocSecuritySetting> {

    /**
     * 过期时长
     */
    private long time;

    /**
     * 唯一码
     */
    private String uniqId;

    /**
     * 提示ID集合
     */
    private List<String> tipId;


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
