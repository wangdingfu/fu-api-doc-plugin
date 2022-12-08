package com.wdf.fudoc.request.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.global.GlobalRequestData;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 【Fu Request】持久化数据对象
 *
 * @author wangdingfu
 * @date 2022-09-18 21:58:24
 */
@Data
@State(name = "FuRequestSettingState", storages = {@Storage("fuRequestSetting.xml")})
public class FuRequestSettingState implements PersistentStateComponent<GlobalRequestData> {

    /**
     * 接口请求数据全局对象 项目级别
     */
    private GlobalRequestData globalRequestData;


    public static FuRequestSettingState getInstance(Project project) {
        return project.getService(FuRequestSettingState.class);
    }

    public static GlobalRequestData getData(Project project) {
        FuRequestSettingState instance = getInstance(project);
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
