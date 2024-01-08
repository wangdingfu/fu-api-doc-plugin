package com.wdf.fudoc.apidoc.sync.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.ShowDocConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ProjectSyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.apidoc.sync.service.ShowDocService;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiConfirmDialog;
import com.wdf.api.base.FuBundle;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.api.notification.FuDocNotification;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.api.util.ProjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 同步接口文档到showDoc接口文档的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:48:07
 */
public class SyncShowDocStrategy extends AbstractSyncApiStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData configData) {
        return false;
    }

    @Override
    protected List<SyncApiResultDTO> doSyncApi(List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData projectRecord) {
        String projectId = apiProjectDTO.getProjectId();
        String projectToken = apiProjectDTO.getProjectToken();
        if (StringUtils.isBlank(projectId) || StringUtils.isBlank(projectToken)) {
            FuDocNotification.notifyError(FuBundle.message("fudoc.sync.showdoc.tip"));
            return Lists.newArrayList();
        }
        //将接口文档数据渲染成markdown格式接口文档
        ShowDocDTO showDocDTO = new ShowDocDTO();
        showDocDTO.setApiKey(apiProjectDTO.getProjectId());
        showDocDTO.setApiToken(apiProjectDTO.getProjectToken());
        showDocDTO.setCategoryName(recursionPath(apiProjectDTO.getSelectCategory()));
        showDocDTO.setTitle(autoTitle(fuDocItemDataList,apiProjectDTO));
        showDocDTO.setContent(FuDocRender.markdownRender(FuDocSetting.getSettingData(), fuDocItemDataList));
        ShowDocService service = ServiceHelper.getService(ShowDocService.class);
        String errorMsg = service.syncApi(showDocDTO, (ShowDocConfigData) configData);
        ApiSyncStatus syncStatus = StringUtils.isBlank(errorMsg) ? ApiSyncStatus.SUCCESS : ApiSyncStatus.FAIL;
        return ObjectUtils.listToList(fuDocItemDataList, f -> buildSyncApiResult(f, apiProjectDTO, syncStatus, errorMsg));

    }


    /**
     * 自动获取接口title
     *
     * @param fuDocItemDataList 接口列表
     * @param apiProjectDTO     项目信息
     * @return showDoc文档title
     */
    private String autoTitle(List<FuDocItemData> fuDocItemDataList, ApiProjectDTO apiProjectDTO) {
        String title = apiProjectDTO.getTitle();
        if (fuDocItemDataList.size() == 1 || StringUtils.isBlank(title)) {
            return fuDocItemDataList.get(0).getTitle();
        }
        return title;
    }

    @Override
    protected ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
        SyncApiConfirmDialog syncApiConfirmDialog = new SyncApiConfirmDialog(ProjectUtils.getCurrProject(), psiClass);
        if (!syncApiConfirmDialog.showAndGet()) {
            //取消则不同步
            return null;
        }
        ApiProjectDTO selected = syncApiConfirmDialog.getSelected();
        selected.setTitle(FuDocUtils.classTitle(psiClass));
        return selected;
    }

}
