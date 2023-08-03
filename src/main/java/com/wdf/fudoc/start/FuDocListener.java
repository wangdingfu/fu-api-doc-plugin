package com.wdf.fudoc.start;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.notification.FuDocNotification;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2023-07-27 10:36:38
 */
@Slf4j
public class FuDocListener implements DynamicPluginListener {

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        log.info("fudoc插件被加载了....");
        FuDocNotification.notifyInfo(FuBundle.message("fudoc.loaded.message"));
    }
}
