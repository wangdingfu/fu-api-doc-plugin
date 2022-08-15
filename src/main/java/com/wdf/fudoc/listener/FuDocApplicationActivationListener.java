package com.wdf.fudoc.listener;


import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.IdeFrame;
import com.wdf.fudoc.FuDocNotification;
import com.wdf.fudoc.view.FuDocGeneralForm;
import org.jetbrains.annotations.NotNull;


/**
 * @author wangdingfu
 * @date 2022-08-14 22:32:40
 */
public class FuDocApplicationActivationListener implements ApplicationActivationListener {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    @Override
    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        LOGGER.info("应用已经启动了");
        FuDocNotification.notifyInfo("【Fu Doc】发布了新版本. 本次新增了许多有用功能和修复了很多历史bug. 快去插件市场升级最新版本吧!");
    }
}
