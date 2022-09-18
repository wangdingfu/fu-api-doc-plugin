package com.wdf.fudoc.request.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.request.global.GlobalRequestData;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 【Fu Request】持久化数据对象
 *
 * @author wangdingfu
 * @date 2022-09-18 21:58:24
 */
@Data
@State(name = "FuRequestState", storages = {@Storage("fuRequest.xml")})
public class FuRequestState implements PersistentStateComponent<FuRequestState> {

    /**
     * 每一个项目的接口请求数据集合
     * key:projectId
     * value:当前项目下所有的请求记录
     */
    private Map<String, GlobalRequestData> moduleRequestDataMap;

    public static FuRequestState getInstance() {
        return ServiceHelper.getService(FuRequestState.class);
    }

    @Override
    public @Nullable FuRequestState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull FuRequestState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
