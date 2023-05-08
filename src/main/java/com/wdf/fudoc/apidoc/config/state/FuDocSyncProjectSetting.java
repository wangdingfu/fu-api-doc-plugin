package com.wdf.fudoc.apidoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.request.state.FuRequestState;
import com.wdf.fudoc.util.ProjectUtils;
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
@State(name = "syncConfig", storages = {@Storage("/fudoc/api-sync-config.xml")})
public class FuDocSyncProjectSetting  implements PersistentStateComponent<SyncApiConfigData> {

    private SyncApiConfigData syncApiConfigData;

    public static SyncApiConfigData getInstance() {
        Project currProject = ProjectUtils.getCurrProject();
        FuDocSyncProjectSetting service = currProject.getService(FuDocSyncProjectSetting.class);
        SyncApiConfigData configData = service.getSyncApiConfigData();
        if(Objects.isNull(configData)){
            configData = new SyncApiConfigData();
        }
        return configData;
    }

    public static List<YApiProjectTableData> getYapiConfigList(){
        return getInstance().getYapiConfigList();
    }

    @Override
    public @Nullable SyncApiConfigData getState() {
        return this.syncApiConfigData;
    }

    @Override
    public void loadState(@NotNull SyncApiConfigData state) {
        this.syncApiConfigData = state;
    }
}
