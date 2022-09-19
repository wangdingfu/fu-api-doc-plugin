package com.wdf.fudoc.request.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.apidoc.config.state.FuDocSecuritySetting;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.request.global.GlobalRequestData;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class FuRequestState implements PersistentStateComponent<FuRequestState> {

    /**
     * 接口请求数据全局对象 项目级别
     */
    private GlobalRequestData globalRequestData;


    public static FuRequestState getInstance(Project project) {
        return project.getService(FuRequestState.class);
    }

    public static GlobalRequestData getRequestData(Project project) {
        if (Objects.nonNull(project)) {
            FuRequestState instance = getInstance(project);
            if (Objects.nonNull(instance)) {
                return instance.getGlobalRequestData();
            }
        }
        return null;
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
