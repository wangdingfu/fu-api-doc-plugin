package com.wdf.fudoc.apidoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import cn.fudoc.common.util.ProjectUtils;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-08 20:31:08
 */
@Data
@State(name = "syncConfig", storages = {@Storage("fuApiSyncConfig.xml")})
public class FuDocSyncProjectSetting implements PersistentStateComponent<SyncApiConfigData> {

    private SyncApiConfigData syncApiConfigData = new SyncApiConfigData();

    public static FuDocSyncProjectSetting getInstance() {
        Project currProject = ProjectUtils.getCurrProject();
        return currProject.getService(FuDocSyncProjectSetting.class);
    }

    public static List<YApiProjectTableData> getYapiConfigList() {
        return getInstance().getState().getYapiConfigList();
    }

    @Override
    public SyncApiConfigData getState() {
        if (Objects.isNull(this.syncApiConfigData)) {
            this.syncApiConfigData = new SyncApiConfigData();
        }
        return this.syncApiConfigData;
    }

    @Override
    public void loadState(@NotNull SyncApiConfigData state) {
        this.syncApiConfigData = state;
    }
}
