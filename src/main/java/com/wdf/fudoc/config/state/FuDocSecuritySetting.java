package com.wdf.fudoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.util.ResourceUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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



    public static FuDocSecuritySetting getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, FuDocSecuritySetting.class);
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
