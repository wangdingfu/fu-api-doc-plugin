package com.wdf.fudoc.start;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
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
        log.info("插件被加载了....");
    }
}
