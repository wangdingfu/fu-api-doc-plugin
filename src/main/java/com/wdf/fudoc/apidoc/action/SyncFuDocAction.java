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
import com.wdf.fudoc.common.enumtype.FuDocAction;


/**
 * 同步接口文档至三方接口文档系统动作
 *
 * @author wangdingfu
 * @date 2022-12-31 22:15:45
 */
public class SyncFuDocAction extends AbstractClassAction {

    @Override
    protected boolean isShow(JavaClassType javaClassType) {
        return JavaClassType.isController(javaClassType);
    }

    @Override
    protected FuDocAction getAction() {
        return null;
    }

    @Override
    protected void execute(AnActionEvent e, PsiClass psiClass, FuDocContext fuDocContext) {
        //获取同步接口文档配置
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        ApiDocSystem apiDocSystem = ApiDocSystem.getInstance(settingData.getEnable());
        //调用同步接口
        SyncFuDocExecutor.sync(apiDocSystem, enableConfigData, fuDocContext, psiClass);
        //加载配置
        FuDocSyncSetting.getInstance().loadState(settingData);
    }
}
