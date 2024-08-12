package com.wdf.fudoc.common.listener;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import cn.fudoc.common.service.FuDocLoadService;
import com.wdf.fudoc.common.constant.FuDocConstants;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

/**
 * @author wangdingfu
 * @date 2023-10-03 14:43:47
 */
public class FuDocLoadListener implements DynamicPluginListener {
    @Override
    public void beforePluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        if (FuDocConstants.ID.equals(pluginDescriptor.getPluginId().getIdString())) {
            ServiceLoader<FuDocLoadService> load = ServiceLoader.load(FuDocLoadService.class, FuDocLoadListener.class.getClassLoader());
            load.forEach(FuDocLoadService::loaded);
        }
    }
}
