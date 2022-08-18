package com.wdf.fudoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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



    public static FuDocSecuritySetting getInstance() {
        return ServiceManager.getService(FuDocSecuritySetting.class);
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
