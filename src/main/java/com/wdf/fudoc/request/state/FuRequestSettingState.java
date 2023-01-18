package com.wdf.fudoc.request.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.request.data.FuRequestSettingData;
import com.wdf.fudoc.request.global.GlobalRequestData;
import com.wdf.fudoc.request.pojo.CommonHeader;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * 【Fu Request】持久化数据对象
 *
 * @author wangdingfu
 * @date 2022-09-18 21:58:24
 */
@Data
@State(name = "FuRequestSettingState", storages = {@Storage("fuRequestSetting.xml")})
public class FuRequestSettingState implements PersistentStateComponent<FuRequestSettingData> {

    /**
     * 【Fu Request】配置数据
     */
    private FuRequestSettingData fuRequestSettingData;


    public static FuRequestSettingState getInstance() {
        return ServiceHelper.getService(FuRequestSettingState.class);
    }

    public static FuRequestSettingData getData() {
        return getInstance().getState();
    }


    @Override
    public FuRequestSettingData getState() {
        if (Objects.isNull(this.fuRequestSettingData)) {
            this.fuRequestSettingData = new FuRequestSettingData();
        }
        return this.fuRequestSettingData;
    }

    @Override
    public void loadState(@NotNull FuRequestSettingData state) {
        this.fuRequestSettingData = state;
    }

}
