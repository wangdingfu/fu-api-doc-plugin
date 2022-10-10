package com.wdf.fudoc.request.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.request.global.GlobalRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 【Fu Request】持久化数据对象
 *
 * @author wangdingfu
 * @date 2022-09-18 21:58:24
 */
@Data
@State(name = "FuRequestState", storages = {@Storage("fuRequest.xml")})
public class FuRequestState implements PersistentStateComponent<GlobalRequestData> {

    /**
     * 接口请求数据全局对象 项目级别
     */
    private GlobalRequestData globalRequestData;


    public static FuRequestState getInstance(Project project) {
        return project.getService(FuRequestState.class);
    }

    public static GlobalRequestData getData(Project project) {
        FuRequestState instance = getInstance(project);
        return instance.getState();
    }

    @Override
    public GlobalRequestData getState() {
        if (Objects.isNull(this.globalRequestData)) {
            this.globalRequestData = new GlobalRequestData();
        }
        return this.globalRequestData;
    }

    @Override
    public void loadState(@NotNull GlobalRequestData state) {
        this.globalRequestData = state;
    }

}
