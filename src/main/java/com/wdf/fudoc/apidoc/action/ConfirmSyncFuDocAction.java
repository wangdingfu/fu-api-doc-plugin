package com.wdf.fudoc.apidoc.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.SyncFuDocExecutor;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.common.AbstractClassAction;

/**
 * @author wangdingfu
 * @date 2023-01-11 18:51:40
 */
public class ConfirmSyncFuDocAction extends AbstractClassAction {

    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.isController(javaClassType);
    }


    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        //获取同步接口文档配置
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        ApiDocSystem apiDocSystem = ApiDocSystem.getInstance(settingData.getEnable());
        fuDocContext.setSyncDialog(false);
        //调用同步接口
        SyncFuDocExecutor.sync(apiDocSystem, enableConfigData, fuDocContext, psiClass);
        //加载配置
        FuDocSyncSetting.getInstance().loadState(settingData);
    }
}
