package com.wdf.fudoc.storage.factory;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.storage.FuRequestConfigStorage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-06-11 21:20:11
 */
public class FuRequestConfigStorageFactory {

    private static final Map<Project, FuRequestConfigStorage> storageMap = new ConcurrentHashMap<>();

    public static FuRequestConfigStorage get(Project project) {
        FuRequestConfigStorage fuRequestConfigStorage = storageMap.get(project);
        if (Objects.isNull(fuRequestConfigStorage)) {
            fuRequestConfigStorage = FuRequestConfigStorage.getInstance(project);
            storageMap.put(project, fuRequestConfigStorage);
        }
        return fuRequestConfigStorage;
    }
}
